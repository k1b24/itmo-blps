package ru.itmo.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration {

    @Bean
    fun sperBankWebClient(): WebClient = WebClient.builder().baseUrl("http://sper-bank:8080").build()

}