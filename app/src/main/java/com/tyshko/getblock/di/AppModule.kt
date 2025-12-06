package com.tyshko.getblock.di

import com.tyshko.getblock.data.repository.RpcRepository
import com.tyshko.getblock.data.repository.RpcRepositoryImpl
import com.tyshko.getblock.view.GetBlockViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::RpcRepositoryImpl){ bind<RpcRepository>() }
    viewModelOf(::GetBlockViewModel)
}