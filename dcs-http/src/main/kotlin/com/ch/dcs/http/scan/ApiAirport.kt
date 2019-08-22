package com.ch.dcs.http.scan

import org.springframework.web.bind.annotation.*
import org.springframework.data.repository.CrudRepository

@RestController("HttpAirportController")
@RequestMapping("/http/airport")
class AirportController(private val repository: AirportRepository) {

    @GetMapping("/")
    fun findAll() = repository.findAllByOrderByCity()

    @GetMapping("/{code}")
    fun findOne(@PathVariable code: String) =
        repository.findByCode(code) ?: throw IllegalArgumentException("Wrong airport code provided")

}

interface AirportRepository : CrudRepository<Airport, Long> {
    fun findByCode(code: String): Airport?
    fun findAllByOrderByCity(): Iterable<Airport>
}