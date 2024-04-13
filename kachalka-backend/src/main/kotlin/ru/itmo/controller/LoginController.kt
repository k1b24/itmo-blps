package ru.itmo.controller

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.service.JwtTokenService

@RestController
@RequestMapping("/v1/login")
class LoginController(
    private val tokenService: JwtTokenService
) {

    @PostMapping
    fun login(authentication: Authentication) =  tokenService.generateToken(authentication)

}