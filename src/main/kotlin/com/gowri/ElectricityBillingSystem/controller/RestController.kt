package com.gowri.ElectricityBillingSystem.controller

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.gowri.ElectricityBillingSystem.*
import com.gowri.ElectricityBillingSystem.connector.HttpSmsSenderConnectorImpl
import com.gowri.ElectricityBillingSystem.service.ApiService
import com.gowri.ElectricityBillingSystem.service.ValidationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.sql.Date
import java.util.*


@RestController
@EnableScheduling
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
    @CrossOrigin(origins = ["http://localhost:4200"])
    @RequestMapping("/readingsForDay")
    fun getReradingsForDay(@RequestParam date: Date): dayReadingResponse{
        println("get data for day" + date)


        val data= apiService.getMeterDataOfTheDayFromRepository("82b1df5638e2940f",date)
        val datapAsInteger:MutableList<getUplinkDataFromDatabaseInteger>? = arrayListOf()

        if(data != null){
            var i=0
            for(a in data){
                println("loop")
                datapAsInteger?.add(i, getUplinkDataFromDatabaseInteger(a.uploadedTime,Integer.parseInt(a.data.substring(0,5))))
                i.inc()

            }

        }


        return dayReadingResponse(datapAsInteger,"S1000")
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
    fun login(): String? {
        val a: JsonObject? = httpConnector.sendMessage()
        println("before" + a)
        val output: String? = a?.get("jwt")?.asString
        return output
    }

    @CrossOrigin(origins = ["http://localhost:4200"])
    @RequestMapping("/loginAdmin")
    fun loginToApplication(@RequestBody f: AdminLogin): LoginResponse{
        println("login end point called")
        println(f.user)
        println(f.password)
        validationService.validateLogin(f.user,f.password)
        return apiService.login(f.user,f.password)

    }

}
