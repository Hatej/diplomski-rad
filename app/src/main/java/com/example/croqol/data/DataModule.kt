package com.example.croqol.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import com.example.croqol.User
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModuleBinds {

    @Binds
    fun provideIndexRepository(repository: NetworkIndexRepository): IndexRepository

}

@Module
@InstallIn(SingletonComponent::class)
object DataModuleProvides {
    @Provides
    @Singleton
    fun provideUserDataStore(
        @ApplicationContext context: Context,
        userSerializer: UserSerializer
    ): DataStore<User> =
        DataStoreFactory.create(
            serializer = userSerializer
        ) {
            context.dataStoreFile("user.pb")
        }

    @Provides
    @Singleton
    fun provideUserSerializer(): UserSerializer = UserSerializer()
}