package com.pconil.testKoin

import org.koin.dsl.module.applicationContext


private val ocastModule = applicationContext {
    bean() { CastManager() as ICastManager }
}

val applicationModule = listOf(
        ocastModule
)