package com.task

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.task.data.dto.recipes.Recipes
import com.task.data.dto.recipes.RecipesItem
import java.io.InputStream
import java.lang.reflect.Type

object TestUtil {
    var dataStatus: DataStatus = DataStatus.Success
    var recipes: Recipes = Recipes(arrayListOf())
    fun initData(): Recipes {
        return Recipes(arrayListOf())
    }

    private fun getJson(path: String): String {
        val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val inputStream: InputStream = ctx.classLoader.getResourceAsStream(path)
        return inputStream.bufferedReader().use { it.readText() }
    }
}
