package com.pconil.testKoin

class Device(
    val name: String,
    val serial:  String )


interface ICastManager {
    fun getDevices(onSuccess: (List<Device>) -> Unit, onError: (Int) -> Unit)
}

class CastManager() : ICastManager {
    private val devices = HashMap<String, Device>()

    init {
        devices["MyDevice"] = Device("MyDevice", "0000")
    }

    override fun getDevices(onSuccess: (List<Device>) -> Unit, onError: (Int) -> Unit){
        onSuccess(devices.values as List<Device>)
    }
}