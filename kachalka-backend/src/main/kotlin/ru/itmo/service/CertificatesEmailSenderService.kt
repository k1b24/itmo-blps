package ru.itmo.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.*

@Service
class CertificatesEmailSenderService(
    private val emailSender: JavaMailSender,
) {

    fun sendCertificateBoughtNotificationEmail(
        login: String,
        email: String,
        certificateId: UUID,
    ) {
        val message = SimpleMailMessage()
        message.setTo(email)
        message.setFrom("kachalka@noreply.com")
        message.setSubject("Поздравляем, вы купили сертификат")
        message.setText(
            """
                Добро пожаловать в качалку, вот твой сертификат
                прихвати чистые трусы и не забудь показать куар код, который мы тебе отправили на входе в зал!
                
                http://109.120.133.57/v1/user/certificates/$certificateId
            """.trimIndent()
        )
        emailSender.send(message)
    }
}