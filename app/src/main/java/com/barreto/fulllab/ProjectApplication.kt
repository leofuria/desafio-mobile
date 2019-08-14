package com.barreto.fulllab

import android.app.Application
import com.barreto.fulllab.di.appModule
import com.barreto.fulllab.di.networkModule
import com.barreto.fulllab.presentation.category.categoryModule
import com.barreto.fulllab.presentation.content.contentModule
//import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ProjectApplication : Application() {

    override fun onCreate() {
        super.onCreate()

//        FirebaseApp.initializeApp(this)

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@ProjectApplication)
            modules(
                listOf(
                    appModule,
                    networkModule,
                    contentModule,
                    categoryModule
                )
            )
        }
    }
}
