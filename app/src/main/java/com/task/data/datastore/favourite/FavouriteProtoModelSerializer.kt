package com.task.data.datastore.favourite

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object FavouriteProtoModelSerializer : Serializer<FavouriteProtoModel> {
    override val defaultValue: FavouriteProtoModel = FavouriteProtoModel.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): FavouriteProtoModel {
        try {
            return FavouriteProtoModel.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: FavouriteProtoModel, output: OutputStream) = t.writeTo(output)
}