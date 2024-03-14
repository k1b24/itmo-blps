package ru.itmo.lab1.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.itmo.lab1.model.request.RegisterNewUserRequest
import ru.itmo.lab1.service.RegisterService

@RestController
@RequestMapping("/register")
class RegisterController(
    private val registerService: RegisterService,
) {

    @PostMapping
    fun registerNewUser(
        @RequestBody registerNewUserRequest: RegisterNewUserRequest,
    ): Mono<Void> = registerService.registerNewUser(registerNewUserRequest)

}