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
class ApiService  {


    @Autowired
    lateinit var uplinkRepository: UplinkRepository
    

    @Autowired
    lateinit var userRepository: UserRepository


    private val logger: Logger = LoggerFactory.getLogger(ApiService::class.java)

    fun saveMeterDataToRepository(data: RecievedData){
        uplinkRepository.insertUplink(UplinkDataToDatabase(data.devEUI,data.data))

    }

    fun getMeterDataFromRepository(devEUI: String): MutableList<getUplinkDataFromDatabase>? {
        return uplinkRepository.getAllUplinkData(devEUI)

    }

    fun getNumberOfUnitsConsumedForCurrentMonth(devEUI: String): Long{
        val lastSavedMeterData  = uplinkRepository.getLastUplinkData(devEUI)
        val date = Date()
        val currentMonth = date.month
        val year = date.year
        val timestamp = Timestamp(year,currentMonth,1,0,0,0,0)
        val meterDataAtStartOfThisMonth = uplinkRepository.getFirstUplinkDataAfterGivenTimeStamp(devEUI,timestamp)
        if(lastSavedMeterData != null && meterDataAtStartOfThisMonth !=null ){
            val numberOfUnitsConsumed = lastSavedMeterData!!.toLong() - meterDataAtStartOfThisMonth!!.toLong()
            return numberOfUnitsConsumed
        }
        else{
            throw ApiException("1002", "INVALID DEV EUI OR NO DATA ",
                 "Error occurred while calulating number of units for current month")
        }


    }



//    override fun login(username: String, password: String): LoginResponse {
//        val currentTime = generateSessionTime(0)
//        var user = userRepository.findByUsername(username.trim())
//
//        if (user == null) {
//            user = userRepository.findByEmail(username.trim())
//        }
//
//        val userTicket = generateTicket(user?.username.toString(), password)
//
//        val isTempLogin = if (user?.password != null) {
//                              !PasswordEncoderFactories.createDelegatingPasswordEncoder()
//                                      .matches(password, user?.password)
//                          } else {
//                                true
//                          }
//
//        val isFirstTimeLogin = (user?.lastLoginTimestamp == null)
//        userRepository.updateUserLastLogin(user?.username,
//                Timestamp(currentTime.year, currentTime.month, currentTime.date,
//                        currentTime.hour, currentTime.min, currentTime.Sec, 0))
//
//        val lastLoginTimeStamp =
//                if (user?.lastLoginTimestamp != null) {
//                    val lastLoginTime =
//                            org.joda.time.DateTime(user.lastLoginTimestamp)
//                    TimeStamp(
//                            lastLoginTime.year,
//                            user.lastLoginTimestamp!!.month + 1,
//                            user.lastLoginTimestamp!!.date,
//                            user.lastLoginTimestamp!!.hours,
//                            user.lastLoginTimestamp!!.minutes,
//                            user.lastLoginTimestamp!!.seconds
//                    )
//                } else {
//                    null
//                }
//
//        val lastPwdResetTimestamp = if (user?.lastPwdResetTimestamp != null) {
//            val lastPwdResetTime =
//                    org.joda.time.DateTime(user.lastPwdResetTimestamp)
//            TimeStamp(
//                    lastPwdResetTime.year,
//                    user.lastPwdResetTimestamp!!.month + 1,
//                    user.lastPwdResetTimestamp!!.date,
//                    user.lastPwdResetTimestamp!!.hours,
//                    user.lastPwdResetTimestamp!!.minutes,
//                    user.lastPwdResetTimestamp!!.seconds
//            )
//        } else {
//            null
//        }
//
//        val loginResponse = LoginResponse(
//                userTicket,
//                lastLoginTimeStamp,
//                lastPwdResetTimestamp,
//                isFirstTimeLogin,
//                isTempLogin
//        )
//
//        logger.info("Successfully received login response [ Login response: {} ]",
//                loginResponse)
//        return loginResponse
//    }
//
     fun register(request: User): User {

        val password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(request.password)

        val user = User(null,request.username,request.email,password,request.accountNum,request.devEUI)

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
