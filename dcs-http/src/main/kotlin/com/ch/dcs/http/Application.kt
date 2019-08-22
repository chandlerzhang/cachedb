package com.ch.dcs.http

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.ch.dcs.http.scan2.DbProperties
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.Banner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(
    scanBasePackages = [
//        "com.ch.dcs.core.scan",
//        "com.ch.dcs.server.scan",
//        "com.ch.dcs.core.webservice.client",
//        "com.ch.dcs.node",
//        "com.ch.dcs.server.config",
//        "com.ch.dcs.server.webservices",
//        "com.spring.webservices",
//        "com.ch.dcs.http.scan"
        "com.ch.dcs.http.scan2"
    ]
)
@EnableConfigurationProperties(DbProperties::class)
open class Application

fun main(args: Array<String>) {
    val root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    root.level = Level.INFO

//    SpringApplicationBuilder(Application::class.java)
//        .bannerMode(Banner.Mode.OFF)
//        .web(WebApplicationType.SERVLET)
//        .run("--spring.config.name=$args")
    runApplication<Application>(*args) {
        this.setBannerMode(Banner.Mode.OFF)
        webApplicationType = WebApplicationType.SERVLET
    }


}
