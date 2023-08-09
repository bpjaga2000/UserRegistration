package com.example.userregistration.module

import android.app.Application
import androidx.room.Room
import com.example.userregistration.api.ApiService
import com.example.userregistration.api.ApiServiceMaker
import com.example.userregistration.db.UserRegistrationDb
import com.example.userregistration.repository.AppRepository
import com.example.userregistration.repository.Repository
import com.example.userregistration.ui.activity.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModule = module {
    single { ApiServiceMaker.invoke() }
}

val databaseModule = module {
    fun provideDatabase(application: Application): UserRegistrationDb {
        return Room.databaseBuilder(
            application,
            UserRegistrationDb::class.java,
            "user_registration"
        ).build()
    }

    single {
        provideDatabase(androidApplication())
    }
}

val repositoryModule = module {
    fun provideRepository(apiService: ApiService, db: UserRegistrationDb): Repository {
        return AppRepository(apiService, db)
    }

    single {
        provideRepository(get(), get())
    }
}

val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}