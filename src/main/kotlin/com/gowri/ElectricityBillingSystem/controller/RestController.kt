package com.gowri.ElectricityBillingSystem.controller

import com.gowri.ElectricityBillingSystem.*
import com.gowri.ElectricityBillingSystem.connector.HttpSmsSenderConnectorImpl
import com.gowri.ElectricityBillingSystem.service.ApiService
import com.gowri.ElectricityBillingSystem.service.ValidationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RestController {
    @Autowired
    private lateinit var httpConnector: HttpSmsSenderConnectorImpl

    @Autowired
    private lateinit var apiService: ApiService

    @Autowired
    private lateinit var validationService: ValidationService


    @RequestMapping("/uplink")
    fun uplink(@RequestBody recievedData:RecievedData){
        println("recieved data" + recievedData)
        apiService.saveMeterDataToRepository(recievedData)
    }

    @RequestMapping("/getNumberOfUnitsForCurrentMonth")
    fun getNumberOfUnitsConsumedForCurrentMonth(@RequestBody devEUI:getNumberOfUnitsConsumedForCurrentMonth){
        println("recieved data" + devEUI.devEUI)
        apiService.getNumberOfUnitsConsumedForCurrentMonth(devEUI.devEUI)
    }

    @RequestMapping("/register")
    fun userRegistration(@RequestBody user: User): User{
        validationService.validateRegistrationRequest(user)
        val user = apiService.register(user)
        return user
    }

    @RequestMapping("/getDeviceReading")
    fun uplink(@RequestBody devEUI: devEUI): MutableList<getUplinkDataFromDatabase>? {
        println("recieved data" + devEUI)
        return apiService.getMeterDataFromRepository(devEUI.devEUI)
    }



    @RequestMapping("/login")
    fun login(){
            val a=httpConnector.sendMessage()
            print(a)
    }

}
