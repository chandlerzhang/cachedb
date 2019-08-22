package com.ch.dcs.http.test

import ch.qos.logback.classic.Level
import com.alibaba.fastjson.JSONObject
import com.ch.dcs.http.PureID
import com.ch.dcs.http.scan2.IdEntity
import com.ch.dcs.http.scan2.InitConfiguration
import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.core.statistics.DefaultStatisticsService
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import org.ehcache.core.spi.service.StatisticsService
import org.slf4j.Logger
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import java.lang.ref.WeakReference
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


fun main() {
    val root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as ch.qos.logback.classic.Logger
    root.level = Level.INFO
    val log = LoggerFactory.getLogger(InitConfiguration::class.java)

    testJvmCache(log)

//    testCache(log)

//    testBlockQueue(log)
}

fun testJvmCache(log: Logger) {
    log.info("start")
    Constants.d.addTable<UserData>(
        "user",
        TableIndex("name", Comparator { o1, o2 -> o1.name.compareTo(o2.name) }),
        TableIndex("name1", Comparator { o1, o2 -> o1.name1.compareTo(o2.name1) }),
        TableIndex("name2", Comparator { o1, o2 -> o1.name2.compareTo(o2.name2) }),
        TableIndex("name3", Comparator { o1, o2 -> o1.name3.compareTo(o2.name3) })
    )
    val c = Constants.d.getTable<UserData>("user")
    passed("prepare cache") {
        repeat(10000) {
            val a = Mock.obj(UserData.tpl, UserData::class.java)
            a.id = PureID.nextId()
            c.add(a)
        }
        log.info("cached ${c.count()} records")
    }
    val first = c.idSet.first()
    val prefix = first.name.substring(0, 1)
    passed("filter by index") {
        var s: Set<UserData> = HashSet()
        s = s.union(
            c.sets["name"]!!.subSet(
                UserData(name = prefix), true,
                UserData(name = prefix + Character.MAX_VALUE), true
            )
        )
        s = s.union(
            c.sets["name1"]!!.subSet(
                UserData(name1 = prefix), true,
                UserData(name1 = prefix + Character.MAX_VALUE), true
            )
        )
        s = s.union(
            c.sets["name2"]!!.subSet(
                UserData(name2 = prefix), true,
                UserData(name2 = prefix + Character.MAX_VALUE), true
            )
        )
        s = s.union(
            c.sets["name3"]!!.subSet(
                UserData(name3 = prefix), true,
                UserData(name3 = prefix + Character.MAX_VALUE), true
            )
        )
        log.info("found ${s.count()} records")
    }
    passed("filter simple") {
        val aa = c.idSet.filter {
            it.name.startsWith(prefix)
                    || it.name1.startsWith(prefix)
                    || it.name2.startsWith(prefix)
                    || it.name3.startsWith(prefix)
        }
        log.info("found ${aa.count()} records")
    }
}

fun passed(message: String, doit: () -> Unit) {
    val log = LoggerFactory.getLogger(InitConfiguration::class.java)
    val startTime = System.currentTimeMillis()
    doit()
    log.info("$message, passed ${System.currentTimeMillis() - startTime} mills")
}

fun testStrongReference(log: Logger) {
    val a = 1
    val b = WeakReference<Int>(a)

    val m = HashMap<Long, String>()
    m.clear()

    val l = LinkedList<String>()
    l.clear()

    log.info("")

    val tm = TreeMap<Long, String>()

    val ts = TreeSet<String>()

}

class Constants {
    companion object {
        @JvmField
        val d = DbData()
    }
}

class DbData {
    val m = HashMap<String, TableData<out IdEntity>>()
    fun <T : IdEntity> getTable(tableName: String): TableData<T> {
        if (!m.containsKey(tableName)) m[tableName] = TableData<T>()
        return m[tableName]!! as TableData<T>
    }

    fun <T : IdEntity> addTable(
        tableName: String,
        vararg indexes: TableIndex<T>
    ) {
        val tds = getTable<T>(tableName).sets
        repeat(indexes.count()) {
            tds[indexes[it].name] = TreeSet(indexes[it].comparator)
        }
    }
}

class TableIndex<T : IdEntity>(var name: String, var comparator: Comparator<T>)

class TableData<T : IdEntity> {
    val idSet = TreeSet<T>(Comparator { o1, o2 -> o1.id.compareTo(o2.id) })
    val sets = HashMap<String, TreeSet<T>>()
    fun add(element: T): Boolean {
        sets.forEach {
            it.value.add(element)
        }
        return idSet.add(element)
    }

    fun count(): Int = idSet.size
}

fun testBlockQueue(log: Logger) {
    val q = ArrayBlockingQueue<Int>(3)
    q.add(1)
    log.info("take 1")
    q.poll()
    log.info("take null")
    val a = q.poll(1, TimeUnit.SECONDS)
    if (a == null) log.info("timeout")
    log.info("take end")
}

fun testCache(log: Logger) {
    log.info("start cache")
    val startTime = System.currentTimeMillis()
    val c = EhCache.getCache("aaa", Long::class.javaObjectType, UserData::class.java)
    printCache(log, EhCache.statisticsService, "aaa")
    repeat(100000) {
        val a = Mock.obj(UserData.tpl, UserData::class.java)
        a.id = PureID.nextId()
//        log.info(a.id.toString())
        c.put(a.id, a)
    }
    log.info("end cache, ${c.count()} records, passed ${System.currentTimeMillis() - startTime} mills")
    printCache(log, EhCache.statisticsService, "aaa")
    val n = c.first().value.name1
    log.info("filter like first name ${c.filter {
        it.value.name1.startsWith(n.subSequence(0, 1))
    }.count()} record")
    val startSortTime = System.currentTimeMillis()
    c.sortedBy {
        it.value.name1
    }
    log.info("sort passed ${System.currentTimeMillis() - startSortTime} mills")

}

fun printCache(log: Logger, s: StatisticsService, name: String) {
    val m = s
        .getCacheStatistics(name)
        .tierStatistics;
    log.info(
        """Cache Info:
        OnHeap Occupied:  ${m["OnHeap"]?.occupiedByteSize}
        OnHeap Allocated: ${m["OnHeap"]?.allocatedByteSize}
        OffHeap Occupied:  ${m["OffHeap"]?.occupiedByteSize}
        OffHeap Allocated: ${m["OffHeap"]?.allocatedByteSize}
    """.trimIndent()
    )
}

object EhCache {

    var statisticsService = DefaultStatisticsService()

    var manager: CacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .using(statisticsService)
        .build()

    init {
        manager.init()
    }

    fun <K, V> getCache(name: String, keyClass: Class<K>, valueClass: Class<V>): Cache<K, V> =
        manager.getCache(name, keyClass, valueClass)
            ?: manager.createCache(
                name,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                    keyClass,
                    valueClass,
                    ResourcePoolsBuilder
                        .heap(1000000)
//                        .offheap(1000, MemoryUnit.MB)

                )
                    .build()
            )
}


object Mock {

    private val manager = ScriptEngineManager()
    private var engine: ScriptEngine

    init {
        val f = Thread.currentThread().contextClassLoader.getResourceAsStream("mock.js")!!
        val bufferedReader = BufferedReader(InputStreamReader(f))
        engine = manager.getEngineByName("JavaScript");
        engine.eval(bufferedReader);
    }

    fun jsonString(template: String) = engine.eval("JSON.stringify(Mock.mock($template))") as String

    fun <T> obj(template: String, clazz: Class<T>): T {
        val a = jsonString(template)
        return JSONObject.parseObject(a, clazz)
    }

}

data class UserData(
    var name1: String = "",
    var name2: String = "",
    var name3: String = "",
    var name4: String = "",
    var name5: String = "",
    var name6: String = "",
    var name7: String = "",
    var name8: String = "",
    var name9: String = "",
    var name10: String = "",
    var name11: String = "",
    var name12: String = "",
    var name13: String = "",
    var name14: String = "",
    var name15: String = "",
    var name16: String = "",
    var name17: String = "",
    var name18: String = "",
    var name19: String = "",
    var name20: String = "",
    var name21: String = "",
    var name22: String = "",
    var name23: String = "",
    var name24: String = "",
    var name25: String = "",
    var name26: String = "",
    var name27: String = "",
    var name28: String = "",
    var name29: String = "",
    var name30: String = "",
    var name31: String = "",
    var name32: String = "",
    var name33: String = "",
    var name34: String = "",
    var name35: String = "",
    var name36: String = "",
    var name37: String = "",
    var name38: String = "",
    var name39: String = "",
    var name40: String = "",
    var name41: String = "",
    var name42: String = "",
    var name43: String = "",
    var name44: String = "",
    var name45: String = "",
    var name46: String = "",
    var name47: String = "",
    var name48: String = "",
    var name49: String = "",
    var name50: String = "",
    var name51: String = "",
    var name52: String = "",
    var name53: String = "",
    var name54: String = "",
    var name55: String = "",
    var name56: String = "",
    var name57: String = "",
    var name58: String = "",
    var name59: String = "",
    var name: String = "",
    override var id: Long = 0
) : IdEntity {
    companion object {
        val tpl: String = """
{
    name1: "@ctitle",
    name2: "@ctitle",
    name3: "@ctitle",
    name4: "@ctitle",
    name5: "@ctitle",
    name6: "@ctitle",
    name7: "@ctitle",
    name8: "@ctitle",
    name9: "@ctitle",
    name10: "@ctitle",
    name11: "@ctitle",
    name12: "@ctitle",
    name13: "@ctitle",
    name14: "@ctitle",
    name15: "@ctitle",
    name16: "@ctitle",
    name17: "@ctitle",
    name18: "@ctitle",
    name19: "@ctitle",
    name20: "@ctitle",
    name21: "@ctitle",
    name22: "@ctitle",
    name23: "@ctitle",
    name24: "@ctitle",
    name25: "@ctitle",
    name26: "@ctitle",
    name27: "@ctitle",
    name28: "@ctitle",
    name29: "@ctitle",
    name30: "@ctitle",
    name31: "@ctitle",
    name32: "@ctitle",
    name33: "@ctitle",
    name34: "@ctitle",
    name35: "@ctitle",
    name36: "@ctitle",
    name37: "@ctitle",
    name38: "@ctitle",
    name39: "@ctitle",
    name40: "@ctitle",
    name41: "@ctitle",
    name42: "@ctitle",
    name43: "@ctitle",
    name44: "@ctitle",
    name45: "@ctitle",
    name46: "@ctitle",
    name47: "@ctitle",
    name48: "@ctitle",
    name49: "@ctitle",
    name50: "@ctitle",
    name51: "@ctitle",
    name52: "@ctitle",
    name53: "@ctitle",
    name54: "@ctitle",
    name55: "@ctitle",
    name56: "@ctitle",
    name57: "@ctitle",
    name58: "@ctitle",
    name59: "@ctitle",
    name: "@ctitle"
}
        """.trimIndent()
    }
}