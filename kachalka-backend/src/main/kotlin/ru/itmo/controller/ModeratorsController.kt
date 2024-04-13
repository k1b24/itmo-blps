package ru.itmo.controller

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.itmo.service.ModeratorService

@RestController
@RequestMapping("/v1/moderators")
class ModeratorsController(
    private val moderatorService: ModeratorService,
) {

    @PostMapping("/{login}")
    fun addModeratorRole(@PathVariable("login") login: String): Mono<Void> =
        moderatorService.addModeratorRoleToUser(login)

}
