package com.ch.dcs.http.scan2

import java.io.Serializable
import javax.persistence.*

@Entity(name = "dcs_h")
data class DcsH(
    var fn: String = "",
    var fd: String = "",
    var fs: String = "",
    var oa: String = "",
    var ri: String = "",
    var rl: String = "",
    var ig: String = "",
    var igc: String = "",
    var seat: String = "",
    var sex: String = "",
    var cna: String = "",
    var ena: String = "",
    @Column(name = "id")
    var idV4: String = "",
    var fn2: String = "",
    var da2: String = "",
    var city2: String = "",
    var flag: String = "",
    var sn: String = "",
    var dd: String = "",
    var ct: String = "",
    var cn: String = "",
    var memo: String = "",
    var tn: String = "",
    var hid: String = "",
    var tel: String = "",
    var eda: String = "",
    var bn: String = "",
    var fg: String = "",
    var age: String = "",
    var feebag: String = "",
    var paymenttype: String = "",
    var paymentinfo: String = "",
    var prepaySeat: String = "",
    var prepayBags: String = "",
    var prepayBaga: String = "",
    var prepayBagw: String = "",
    var prepayFees: String = "",
    var p01: String = "",
    var p02: String = "",
    var p03: String = "",
    var p04: String = "",
    var p05: String = "",
    var p06: String = "",
    var p07: String = "",
    var p08: String = "",
    var p09: String = "",
    var p10: String = "",
    var p11: String = "",
    var p12: String = "",
    var p13: String = "",
    var msg: String = "",
    var rd: String = "",
    var feebaa: String = "",
    var doca: String = "",
    var doco: String = "",
    var docs: String = "",
    var edt: String = "",
    var sm: String = "",
    var idea: String = "",
    var topic: String = "",
    var fd2: String = "",
    var ptflag: String = "",
    var doct: String = "",
    var barcode: String = "",
    var cktype: String = "",
    var docx: String = "",
    @Column(name = "id_v8")
    var idV8: String = "",
    var idDb: String = ""
) : IdEntity, Serializable {
    @Id
    @GeneratedValue
    @Column(name = "sid")
    override var id: Long = 0L
}

interface IdEntity {
    var id: Long
}
