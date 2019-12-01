package com.gowri.ElectricityBillingSystem.service

import com.gowri.ElectricityBillingSystem.*
import com.gowri.ElectricityBillingSystem.dao.UplinkRepository
import com.gowri.ElectricityBillingSystem.dao.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*


@Service
class ApiService {


    @Autowired
    lateinit var uplinkRepository: UplinkRepository

    @Autowired
    lateinit var dataDecoder: DataDecoder


    @Autowired
    lateinit var userRepository: UserRepository


    private val logger: Logger = LoggerFactory.getLogger(ApiService::class.java)

    fun saveMeterDataToRepository(data: RecievedData) {
        val decodedData = dataDecoder.decodeDataFromDevice(data.data)
        uplinkRepository.insertUplink(UplinkDataToDatabase(data.devEUI, decodedData))

    }

    fun getMeterDataFromRepository(devEUI: String): MutableList<getUplinkDataFromDatabase>? {
        return uplinkRepository.getAllUplinkData(devEUI)

    }

    fun getMeterDataOfTheDayFromRepository(devEUI: String,date: java.sql.Date): MutableList<getUplinkDataFromDatabase>? {
        return uplinkRepository.getAllUplinkDataForDay(devEUI,date)

    }

    fun getNumberOfUnitsConsumedForCurrentMonth(devEUI: String): Long {
        val lastSavedMeterData = uplinkRepository.getLastUplinkData(devEUI)
        val date = Date()
        val currentMonth = date.month
        val year = date.year
        val timestamp = Timestamp(year, currentMonth, 1, 0, 0, 0, 0)
        val meterDataAtStartOfThisMonth = uplinkRepository.getFirstUplinkDataAfterGivenTimeStamp(devEUI, timestamp)
        if (lastSavedMeterData != null && meterDataAtStartOfThisMonth != null) {
            val numberOfUnitsConsumed = lastSavedMeterData!!.toLong() - meterDataAtStartOfThisMonth!!.toLong()
            return numberOfUnitsConsumed
        } else {
            throw ApiException("1002", "INVALID DEV EUI OR NO DATA ",
                    "Error occurred while calulating number of units for current month")
        }


    }


    fun login(email: String, password: String): LoginResponse {
        val user = userRepository.findByEmail(email)


        val loginResponse = LoginResponse(
                "id",
                true
        )

        logger.info("Successfully received login response [ Login response: {} ]",
                loginResponse)
        return loginResponse
    }

    fun register(request: User): User {

        val password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(request.password)

        val user = User(null, request.username, request.email, password, request.accountNum, request.devEUI)

        val registrationResponse = userRepository.create(user)

        logger.info("Successfully received registration response [ Registration response: {} ]",
                registrationResponse)

        return registrationResponse
    }
//
//
//
//
//    private fun sendEmail(model: HashMap<String, Any>, email: String, templateName: String, subject: String) {
//        val mail = Mail(email, subject, model, templateName)
//        emailService.sendEmail(mail)
//    }
//
//    fun generateSessionTime(date: Int): TimeStamp {
//        val currentDate = Date()
//
//        // convert date to calendar
//        val calendar = Calendar.getInstance()
//        calendar.time = currentDate
//
//        // manipulate date
//        calendar.add(Calendar.DATE, date)
//
//        // convert calendar to date
//        val currentDatePlusOne = calendar.time
//
//        return TimeStamp(currentDatePlusOne.year,
//                currentDatePlusOne.month,
//                currentDatePlusOne.date,
//                currentDatePlusOne.hours,
//                currentDatePlusOne.minutes,
//                currentDatePlusOne.seconds
//        )
//    }
}
