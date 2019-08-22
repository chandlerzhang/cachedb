package com.ch.dcs.http.scan

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class Configuration {

    @Bean
    open fun databaseInitializer(
        userRepository: UserRepository,
        airportRepository: AirportRepository
    ) = ApplicationRunner {

        airportRepository.save(
            Airport(
                code = "PVG",
                display = "浦东",
                city = "shanghai",
                cityDisplay = "上海"
            )
        )

        airportRepository.save(
            Airport(
                code = "SHA",
                display = "虹桥",
                city = "shanghai",
                cityDisplay = "上海"
            )
        )

        userRepository.save(
            User(
                airportCode = "SHA",
                workId = "002387",
                password = "000000",
                display = "张三",
                phoneNumber = "12321321321"
            )
        )
    }
}
