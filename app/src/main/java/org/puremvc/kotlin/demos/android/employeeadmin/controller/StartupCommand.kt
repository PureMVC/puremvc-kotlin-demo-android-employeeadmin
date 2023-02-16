//
//  StartupCommand.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.controller

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.puremvc.kotlin.demos.android.employeeadmin.ApplicationFacade
import org.puremvc.kotlin.demos.android.employeeadmin.BuildConfig
import org.puremvc.kotlin.demos.android.employeeadmin.model.RoleProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.UserProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.service.Error
import org.puremvc.kotlin.demos.android.employeeadmin.model.service.RoleService
import org.puremvc.kotlin.demos.android.employeeadmin.model.service.UserService
import org.puremvc.kotlin.demos.android.employeeadmin.view.ApplicationMediator
import org.puremvc.kotlin.multicore.interfaces.INotification
import org.puremvc.kotlin.multicore.patterns.command.SimpleCommand
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class StartupCommand: SimpleCommand() {

    override fun execute(notification: INotification) {
        try {
            val client = OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(15, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
                .addInterceptor { it.proceed(it.request().newBuilder()
                    .header("User-Agent:", "Android").build()) }
                .build()

            val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:8080")
                .client(client).addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
                .build()

            val converter: Converter<ResponseBody, Error> = retrofit.responseBodyConverter(Error::class.java, arrayOfNulls<Annotation>(0))

            facade.registerProxy(UserProxy(retrofit.create(UserService::class.java), converter))
            facade.registerProxy(RoleProxy(retrofit.create(RoleService::class.java), converter))

            facade.registerCommand(ApplicationFacade.REGISTER) { RegisterCommand() }
            facade.registerMediator(ApplicationMediator(WeakReference(notification.body)))
        } catch (e: Exception) {
            println(e)
        }
    }

}