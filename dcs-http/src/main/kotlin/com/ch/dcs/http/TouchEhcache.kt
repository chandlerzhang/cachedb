package com.ch.dcs.http

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.CacheConfiguration
import org.ehcache.config.builders.*
import org.ehcache.config.units.MemoryUnit
import org.ehcache.core.EhcacheManager
import org.ehcache.expiry.ExpiryPolicy
import org.ehcache.config.units.EntryUnit
import org.slf4j.LoggerFactory


fun main() {

    val root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    root.level = Level.ERROR

    val cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build() as CacheManager
    cacheManager.init()

    val myCache = cacheManager.createCache(
        "myCache",
        CacheConfigurationBuilder.newCacheConfigurationBuilder(
            Long::class.javaObjectType,
            String::class.java,
            ResourcePoolsBuilder
                .heap(1)
                .offheap(100, MemoryUnit.MB)
//                .disk(20, MemoryUnit.MB, true)
        )
//            .withExpiry(ExpiryPolicyBuilder.noExpiration())
//            .withSizeOfMaxObjectGraph(500)
//            .withSizeOfMaxObjectSize(500, MemoryUnit.B)
            .build()
    )

    myCache.put(1L, "da one!")
    myCache.put(2L, "da 2!")
    myCache.put(3L, "da 3!")
    myCache.put(4L, "da 4!")
    val value = myCache.get(4L)

    println(value)

    cacheManager.removeCache("myCache")

    cacheManager.close()

}
