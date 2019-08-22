package com.ch.dcs.http.scan2

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

////@Configuration
//@ConfigurationProperties("dcs")
//open class Properties {
//    lateinit var appNum: String
//}

//@Component
@ConfigurationProperties("dcs")
class DbProperties {
    lateinit var aaa: String
    val datasource = DbConfig()

}

class DbConfig {
    lateinit var url: String
    lateinit var username: String
    lateinit var password: String
    var validationQuery: String? = null
    var driverClassName: String? = null
}

