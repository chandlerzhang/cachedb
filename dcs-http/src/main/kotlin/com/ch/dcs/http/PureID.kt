package com.ch.dcs.http

import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread


fun main(args: Array<String>) {
    val m = ConcurrentHashMap<Long, Long>()
    repeat((1..10000).count()) {
        thread(start = true) {
            println("thread ${Thread.currentThread()} is started")
            repeat((1..1000).count()) {
                val id = PureID.nextId()
//                println(id)
                if(m.containsKey(id)){
                    println("duplicate id $id")
                }
                m[id] = id
            }
            println("thread ${Thread.currentThread()} is over")
        }
    }
}

object PureID {

    const val TIME = "time"
    const val SERIAL = "serial"
    const val MACHINE = "machine"
    const val APP = "app"
    const val TIME_UNIT_MILLISECOND = "ms"
    const val TIME_UNIT_SECOND = "s"

    private val timeUnit = TIME_UNIT_MILLISECOND
    private val timeBits: Int = 41
    private val serialBits: Int = 7
    private val machineBits: Int = 4
    private val appBits: Int = 1
    private val machineId: Int = 0
    private val appId: Int = 0

    private var currentTimestamp: Long = 0
    private var serial: Long = 0
    private var maxTimePart: Long = 0
    private var maxSerial: Long = 0

    init {
        maxTimePart = (1.toLong() shl timeBits)
        maxSerial = (1.toLong() shl serialBits - 1)
    }

    /**
     * 获取下一个id
     */
    @Synchronized
    fun nextId(): Long {
        // 如果时间戳变化了，重置序号为0
        val now = getCurrentTimestamp()
        if (now > currentTimestamp) {
            currentTimestamp = now
            serial = 0
        }

        // 如果序号用完了，等到下一下时间戳, 并重置序号为0
        if (serial > maxSerial) {
            var nextTimestamp: Long
            do {
                nextTimestamp = getCurrentTimestamp()
            } while (nextTimestamp == currentTimestamp)
            currentTimestamp = nextTimestamp
            serial = 0
        }
        // 生成 ID

        var id: Long = 0
        id += currentTimestamp % maxTimePart shl serialBits + machineBits + appBits
        id += serial shl machineBits + appBits
        id += machineId shl appBits
        id += appId

        // 序号加1
        serial += 1

        return id
    }

    /**
     * id 中解析时间戳毫秒数
     */
    fun parseTimestamp(id: Long): Long {
        var timestamp = getCurrentTimestamp()
        timestamp -= maxTimePart
        val highPart = timestamp - timestamp % maxTimePart
        return highPart + (id shr serialBits + machineBits + appBits)
    }

    /**
     * id 中解析machineId
     */
    fun parseMachineId(id: Long): Long {
        return (id shr appBits) - (id shr machineBits + appBits shl machineBits)
    }

    /**
     * id 中解析appId
     */
    fun parseAppId(id: Long): Long {
        return id - (id shr appBits shl appBits)
    }

    /**
     * 获取时间戳
     * @return
     */
    private fun getCurrentTimestamp(): Long {
        var timestamp = System.currentTimeMillis()
        if (TIME_UNIT_SECOND.equals(timeUnit, ignoreCase = true)) {
            timestamp /= 1000
        }
        return timestamp
    }

    internal class ConfItem(var name: String, var bitLength: Int)
}
