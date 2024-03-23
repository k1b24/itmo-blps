package ru.itmo.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean

class ObjectMapperConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()

}