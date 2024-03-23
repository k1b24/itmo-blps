package ru.itmo.qr.validator.service.configuration

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfiguration {

    @Bean
    fun kachalkaBackendRestTemplate(): RestTemplate = RestTemplateBuilder().rootUri("http://localhost:8080").build()
}