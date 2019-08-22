package com.ch.dcs.http.scan

import javax.persistence.*

@Entity
class Airport(
    var code: String,
	var display: String,
    var city: String,
    var cityDisplay: String
) : CommonEntity()

@Entity
class User(
    airportCode: String,
    var workId: String,
    var password: String,
    var display: String? = "",
    var phoneNumber: String? = ""
) : CommonOwnAirportEntity(airportCode)

@MappedSuperclass
abstract class CommonOwnAirportEntity(var ownAirport: String) : CommonEntity()

@MappedSuperclass
abstract class CommonEntity : IdEntity() {
    var whrDel: Boolean = false
    var updateTime: String = ""
}

@MappedSuperclass
abstract class IdEntity {
    @Id
    @GeneratedValue
    var id: Long? = null
}
