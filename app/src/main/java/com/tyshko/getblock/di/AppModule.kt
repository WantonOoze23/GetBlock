package com.tyshko.getblock.di

import com.tyshko.getblock.data.repository.RpcRepository
import com.tyshko.getblock.data.repository.RpcRepositoryImpl
import com.tyshko.getblock.view.GetBlockViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    encodeDefaults = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
            }
        }
    }
    single<RpcRepository> {
        RpcRepositoryImpl(get())
    }
    viewModel {
        GetBlockViewModel(get())
    }
}