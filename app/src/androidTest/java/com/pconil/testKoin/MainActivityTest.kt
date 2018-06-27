package com.pconil.testKoin

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.whenever
import com.pconil.testKoin.R
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.koin.test.createMock


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest: KoinTest {

    val myBeanToMock: ICastManager by inject()

    @Rule
    @JvmField
    //As interaction with mock starts in activity's onCreate we can't launch it before mock configuration
    val rule = object : ActivityTestRule<MainActivity>(MainActivity::class.java, false, false) {}

    @Before
    fun setUp() {
        loadKoinModules(applicationModule)
        createMock<ICastManager>()
    }

    @After
    fun tearDown() {
        rule.finishActivity()
        closeKoin()
    }

    @Test
    fun verifyMockInjection() {
        // We want to capture lambda callbacks given as argument to the mock to interact with it's caller

        doAnswer {
            //arguments[0] is the onSuccess method
            @Suppress("UNCHECKED_CAST")
            (it.arguments[0] as (List<Device>) -> Unit).invoke(listOf(Device("myMockedDevice", "2000")))
        }.whenever(myBeanToMock).getDevices(any(), any())

        rule.launchActivity(null)
        BaristaVisibilityAssertions.assertDisplayed(R.string.my_mocked_device)
    }
}
