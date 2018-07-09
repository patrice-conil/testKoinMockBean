package com.pconil.koinTest

import com.pconil.testKoin.applicationModules
import org.junit.Test
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.test.KoinTest
import org.koin.test.dryRun

class MyTest : KoinTest {
    @Test
    fun diDryRun(){
        // start Koin
        startKoin(applicationModules)
        // dry run of given module list
        dryRun()
    }
}