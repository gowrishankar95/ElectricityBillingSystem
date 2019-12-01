package com.gowri.ElectricityBillingSystem.service

import org.springframework.stereotype.Service
import java.util.*

@Service
class DataDecoder {
    fun decodeDataFromDevice(data:String): String{
        val decodedData = Base64.getDecoder().decode(data)
        var output =""
        for (a in decodedData){
            output=output+Character.toString(a.toChar())

        }

        return output
    }

}
