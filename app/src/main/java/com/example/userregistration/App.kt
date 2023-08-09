package com.example.userregistration

import android.app.Application
import com.example.userregistration.module.apiModule
import com.example.userregistration.module.databaseModule
import com.example.userregistration.module.repositoryModule
import com.example.userregistration.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                apiModule,
                repositoryModule,
                databaseModule,
                viewModelModule
            )
        }
    }

}