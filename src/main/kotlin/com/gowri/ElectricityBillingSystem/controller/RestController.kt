package com.gowri.ElectricityBillingSystem.controller

import com.gowri.ElectricityBillingSystem.RecievedData
import com.gowri.ElectricityBillingSystem.connector.HttpConnector
import com.gowri.ElectricityBillingSystem.connector.HttpSmsSenderConnectorImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@RestController
class RestController {
    @Autowired
    private lateinit var httpConnector: HttpSmsSenderConnectorImpl

    @RequestMapping("/uplink")
    fun uplink(@RequestBody ab:RecievedData){
            print("end point called" +ab);
    }

    @RequestMapping("/login")
    fun login(){
            val a=httpConnector.sendMessage()
            print(a)
    }

}
