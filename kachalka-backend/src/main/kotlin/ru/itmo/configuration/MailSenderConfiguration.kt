package ru.itmo.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class MailSenderConfiguration {

    @Bean
    fun mailSender(): JavaMailSenderImpl = JavaMailSenderImpl().apply {
        protocol = "smtp"
        host = "smtp.gmail.com"
        port = 587
        username = "lazeevsergeyps@gmail.com"
        password = "ijdjtwpoyfsqnodc"
        javaMailProperties.setProperty("mail.smtp.auth", "true")
        javaMailProperties.setProperty("mail.smtp.starttls.enable", "true")
    }

}