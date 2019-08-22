package com.ch.dcs.http.scan2

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.alibaba.druid.pool.DruidDataSource
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.config.units.MemoryUnit
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource
import kotlin.math.log

@Configuration
open class InitConfiguration(
    private val dbProperties: DbProperties
) {

    private val log = LoggerFactory.getLogger(InitConfiguration::class.java)

    @Autowired
    lateinit var hRepo: DcsHRepository


    @Bean
    open fun doSomething(): CommandLineRunner {
        return CommandLineRunner {
//            println("----------------------------------------------")
//            println(dbProperties.aaa)

            log.info("count is ${hRepo.count()}")


//            val l = hRepo.findAllById(listOf(65102310, 64961246));
            val l = hRepo.findAll()

//            println("count2 is ${l.count()}")

            val cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build() as CacheManager
            cacheManager.init()

            val myCache = cacheManager.createCache(
                "myCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                    Long::class.javaObjectType,
                    DcsH::class.java,
                    ResourcePoolsBuilder
                        .heap(1000000)
//                        .offheap(1000, MemoryUnit.MB)
//                .disk(20, MemoryUnit.MB, true)
                )
//            .withExpiry(ExpiryPolicyBuilder.noExpiration())
//            .withSizeOfMaxObjectGraph(500)
//            .withSizeOfMaxObjectSize(500, MemoryUnit.B)
                    .build()
            )
            log.info("start to cache")
            l.forEach {
                if (it == null) {
                    log.info(it.toString())
                    return@forEach
                }
//                println(it.sid)
                myCache.put(it.id, it)
            }
            log.info("end to cache")

            log.info("count2 is ${myCache.count()}")

//            myCache.sortedBy {
//                it.value.rl
//            }
            log.info(myCache.filter {
                it.value.fn == "9C8724"
            }.first().toString())

            cacheManager.removeCache("myCache")

            cacheManager.close()
        }
    }
}
