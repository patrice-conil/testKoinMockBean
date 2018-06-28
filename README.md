# AndroidTest using koin createMock to capture lambda as parameter

This sample demonstrates how to use koin createMock() in androidTest to capture then call lambda given as parameter of one mocked bean.
    
    class CastManager() : ICastManager {
        private val devices = HashMap<String, Device>()

        init {
            devices["MyDevice"] = Device("MyDevice", "0000")
        }
    
        override fun getDevices(onSuccess: (List<Device>) -> Unit, onError: (Int) -> Unit){
            onSuccess(devices.values as List<Device>)
        }
    }

The main activity needs an instance of castManager in its onCreate.

    class MainActivity: AppCompatActivity() {
    
        private val castManager by inject<ICastManager>()
    
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            setSupportActionBar(toolbar)
    
            castManager.getDevices(
                            {devices: List<Device> -> onSuccess(devices)},
                            {error: Int -> onError(error)} )
    
            fab.setOnClickListener { view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
    ...
    
The real bean is declared as usual like this ...

    val applicationModules = listOf(
            module {
                single() { CastManager() as ICastManager }
            }
    )

And koin is started by the application
    
    open class MyApplication: Application() {
    
        override fun onCreate() {
            startKoin(this, applicationModules, logger = if (BuildConfig.DEBUG) AndroidLogger() else EmptyLogger())
            super.onCreate()
        }
    }
    
Since koin-1.0.0-alpha23 we can use createMock() to inject mock instance 

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

