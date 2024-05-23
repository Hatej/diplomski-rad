package com.example.croqol.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.croqol.User
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

@Singleton
class UserSerializer : Serializer<User> {
    override val defaultValue: User = User.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): User {
        try {
            return User.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }
    override suspend fun writeTo(t: User, output: OutputStream) = t.writeTo(output)
}