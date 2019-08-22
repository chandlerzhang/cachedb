package com.ch.dcs.http.scan2

import com.alibaba.druid.pool.DruidDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
open class DbConfiguration(
    private val dbProperties: DbProperties
) {

    @Bean
    open fun dataSource(): DataSource {
        val dataSource = DruidDataSource()
        dataSource.url = dbProperties.datasource.url;
        dataSource.username = dbProperties.datasource.username;
        dataSource.password = dbProperties.datasource.password;
        dataSource.validationQuery = dbProperties.datasource.validationQuery;
        return dataSource
    }

}
