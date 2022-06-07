package com.task.utils.logs

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.task.App
import com.task.BuildConfig
import java.io.File
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.text.Regex.Companion.escapeReplacement
/**
 * This class to be used for adding logs in application
 */

object AppLogger {
    private val LOG_TAG = "app"
    private val APP_LOG_FILE = "/application_log.txt"
    private val BLACK_LISTED_KEYWORDS = arrayOf(
        "passPhrase",
        "password",
        "serialNumber",
        "key",
        "newKey",
        "email",
        "newPassPhrase",
        "newPassword"
    )

    /*
    regex 1: \\.*keyword=[^;]{1,14} // mask as keyword=***; or keyword=***
    regex 2: \\.*keyword = [^\n]{1,14} // mask as keyword = ***
    regex 3: \.*<keyword>[^</]* // mask as <keyword>***</keyword>
     */
    private val LEFT_REGEXS = arrayOf(
        "\\.*",
        "\\.*",
        "\\.*<"
    )
    private val RIGHT_REGEXS = arrayOf(
        "=[^;|,| ]{1,14}",  // mask as keyword=***; or keyword=***
        " = [^\n|;|,| ]{1,14}",  // mask as keyword = ***
        ">[^</]*" // mask as <keyword>***</keyword>
    )

    fun d(TAG: String, message: String) {
        Log.d(TAG, message)
    }

    fun v(TAG: String, message: String) {
        Log.v(TAG, message)
    }

    fun i(TAG: String, message: String) {
        Log.i(TAG, message)
    }

    fun e(TAG: String, message: String, throwable: Throwable?) {
        Log.e(TAG, message, throwable)
    }

    fun printLog(
        callerClassName: String,
        msg: String,
        type: LogType = LogType.D,
        throwable: Throwable? = null
    ) {
        var isErrorLog = false
        when (type) {
            LogType.D -> AppLogger.d(
                LOG_TAG,
                callerClassName + ":: " + escapeReplacement(sanitizeLog(msg))
            )
            LogType.I -> AppLogger.i(
                LOG_TAG,
                callerClassName + ":: " + escapeReplacement(sanitizeLog(msg))
            )
            LogType.V -> AppLogger.v(
                LOG_TAG,
                callerClassName + ":: " + escapeReplacement(sanitizeLog(msg))
            )
            LogType.E -> {
                AppLogger.e(
                    LOG_TAG,
                    callerClassName + ":: " + escapeReplacement(sanitizeLog(msg)),
                    throwable
                )
                isErrorLog = true
            }
        }
        uploadLogs(
            type,
            LOG_TAG + "/" + callerClassName + ":: " + escapeReplacement(sanitizeLog(msg)) + " " + (throwable?.stackTraceToString()
                ?: ""),
            isErrorLog
        )                                                                                           // Each log would be sorted and saved to server
    }

    private fun uploadLogs(type: LogType, msg: String, isErrorLog: Boolean) {
        val remoteLogDb = Firebase.firestore
        val userId = "userId 01"
        val date = Calendar.getInstance().time.toString("dd-MM-yyyy")
        val time = Calendar.getInstance().time.toString("hh:mm:ss a")

        if (isErrorLog) createUploadLogFile(App.appContext, userId, time)

        val logEntry = hashMapOf(
            LogConstatnts.LOG_TYPE to type,
            time to msg,
            LogConstatnts.LOG_FILE_PATH to ""
        )

        remoteLogDb.collection(LogConstatnts.LOG_TABLE_NAME).document(date).collection("userId 01").document(time)
            .set(logEntry)
            .addOnFailureListener {
                Log.e(LOG_TAG, "addOnFailureListener: " + it.message)
            }
    }

    private fun uploadLogFile(context: Context, userId: String? = null, logTime: String? = null) {
        val ref = FirebaseStorage.getInstance().reference
        val date = Calendar.getInstance().time.toString("dd-MM-yyyy")
        val time = Calendar.getInstance().time.toString("hh:mm:ss a")

        val file = Uri.fromFile(File(context.cacheDir, APP_LOG_FILE))
        val dbPathReference = ref.child("app_log_files/${date}/userId_01/${logTime?:time}/${file.lastPathSegment}")
        val uploadTask = dbPathReference.putFile(file)

        uploadTask.addOnFailureListener {
            Log.e(LOG_TAG, "updateLogFile: OnFailure " + it.message)
        }.addOnSuccessListener { taskSnapshot ->
            Log.e(LOG_TAG, "updateLogFile: log file uploaded ")
            val filePath = taskSnapshot.metadata?.path
            if (userId!=null && logTime!=null && filePath!=null && filePath.isNotEmpty())
                Firebase.firestore.collection(LogConstatnts.LOG_TABLE_NAME).document(date).collection(userId).document(logTime).update(
                    mapOf(
                        LogConstatnts.LOG_FILE_PATH to taskSnapshot.metadata?.path
                    )
                )
        }
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    //Returns the sanitized log message which blacklisted keywords are masked
    private fun sanitizeLog(msg: String): String {
        var textMsg = msg
        if (BuildConfig.DEBUG)
            return msg

        for (i in BLACK_LISTED_KEYWORDS.indices) {
            val msgLowerCase = msg.lowercase(Locale.getDefault())
            if (msgLowerCase.contains(BLACK_LISTED_KEYWORDS.get(i)))
                textMsg = returnMaskedLog(msg, BLACK_LISTED_KEYWORDS.get(i))
        }
        return textMsg
    }

    /**
     * return the log which the given black list keyword is masked
     * @param maskedMsg
     * @param blackListKeyword
     */
    private fun returnMaskedLog(maskedMsg: String, blackListKeyword: String): String {
        var maskedMsg = maskedMsg
        val originalMsg = maskedMsg
        for (i in LEFT_REGEXS.indices) {
            maskedMsg = maskBlackListedKeyword(
                maskedMsg,
                LEFT_REGEXS.get(i),
                RIGHT_REGEXS.get(i),
                blackListKeyword
            )
            if (!maskedMsg.equals(originalMsg, ignoreCase = true)) return maskedMsg
        }
        return maskedMsg
    }

    /**
     * mask the given black list keyword's information
     * @param msg
     * @param leftRegex
     * @param rightRegex
     * @param blackListKeyword
     */
    private fun maskBlackListedKeyword(
        msg: String,
        leftRegex: String,
        rightRegex: String,
        blackListKeyword: String
    ): String {
        var txtMsg = msg
        val pattern =
            Pattern.compile(leftRegex + blackListKeyword + rightRegex, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(msg)
        while (matcher.find()) {
            txtMsg = if (rightRegex.equals(RIGHT_REGEXS.get(2), ignoreCase = true))
                (msg.substring(
                    0,
                    matcher.start() + blackListKeyword.length + 1
                ) + ">***" + msg.substring(matcher.end()))
            else if (rightRegex.equals(RIGHT_REGEXS.get(1), ignoreCase = true))
                (msg.substring(
                    0,
                    matcher.start() + blackListKeyword.length
                ) + " = ***" + msg.substring(matcher.end()))
            else
                (msg.substring(
                    0,
                    matcher.start() + blackListKeyword.length
                ) + "=***" + msg.substring(matcher.end()))
        }
        return txtMsg
    }

    //Clear file and save last 10k lines of logs
    private fun createUploadLogFile(appContext: Context, userId: String? = null, logTime: String? = null) {
        try {
            val filename =
                File(appContext.cacheDir, APP_LOG_FILE)
            if (!filename.createNewFile()) {
                clearFile(filename)
            }
            val cmd = "logcat *:D -t 10000 -v time -f " + filename.absolutePath
            val p = Runtime.getRuntime().exec(cmd)
            p.waitFor()
            clearLogcat()
            uploadLogFile(appContext, userId, logTime)                                                               // After generating log file, upload it to server
        } catch (e: Exception) {
            printLog(
                LOG_TAG,
                "sendLogs -> Exception" + e.message,
                LogType.E,
                e
            )
        }
    }

    // Clear logcat
    private fun clearLogcat() {
        try {
            val cmd = "logcat -c"
            Runtime.getRuntime().exec(cmd)
        } catch (e: Exception) {
            printLog(
                LOG_TAG,
                "clearLogcat -> Exception" + e.message,
                LogType.E,
                e
            )
        }
    }

    private fun clearFile(file: File) {
        try {
            PrintWriter(file).use { printWriter -> printWriter.print("") }
        } catch (e: Exception) {
            printLog(
                LOG_TAG,
                "clearFile -> Exception" + e.message,
                LogType.E,
                e
            )
        }
    }

    class LogConstatnts {
        companion object {
            const val LOG_FILE_PATH = "fileUri"
            const val LOG_TYPE = "type"
            const val LOG_TABLE_NAME = "app_logs"
        }
    }

    enum class LogType {
        D, I, V, E
    }
}

fun Any.getClassTag(): String = this.javaClass.simpleName