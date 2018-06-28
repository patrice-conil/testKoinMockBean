package com.pconil.testKoin

import org.koin.dsl.module.module

val applicationModules = listOf(
        module {
            single() { CastManager() as ICastManager }
        }
)
