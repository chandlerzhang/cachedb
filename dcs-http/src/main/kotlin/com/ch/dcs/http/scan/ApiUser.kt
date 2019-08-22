package com.ch.dcs.http.scan

import org.springframework.data.repository.CrudRepository
import org.springframework.web.bind.annotation.*

@RestController("HttpUserController")
@RequestMapping("/http/user")
class UserController(private val repository: UserRepository) {

    @GetMapping("/")
    fun queryUser() = repository.findAll()

    @GetMapping("/{workId}")
    fun findOne(@PathVariable workId: String) = repository.findByWorkId(workId)
}

interface UserRepository : CrudRepository<User, Long> {
    fun findByWorkId(workId: String): User
}
