package com.ch.dcs.http.scan

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("dcs")
class Properties {

	lateinit var appNum: String
	val message = Message()

	class Message {
		var app: String? = null
		lateinit var stowage: String
	}

}
