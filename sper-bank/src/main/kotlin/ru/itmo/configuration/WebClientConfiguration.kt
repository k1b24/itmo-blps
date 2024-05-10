package ru.itmo.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@ConfigurationProperties("kachalka")
class WebClientConfiguration {

    lateinit var baseUrl: String

    @Bean
    fun kachalkaWebClient(): WebClient = WebClient.builder().baseUrl(baseUrl).build()
}