package com.gowri.ElectricityBillingSystem.scheduler

import java.text.SimpleDateFormat
import java.util.Date

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledTasks {

    private val log = LoggerFactory.getLogger(ScheduledTasks::class.java)

    private val dateFormat = SimpleDateFormat("HH:mm:ss")

    @Scheduled(cron = "0 49 17 * * ?")
    fun reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(Date()))
        println("hello form scheduler")
    }



}
