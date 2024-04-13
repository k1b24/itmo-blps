package ru.itmo.configuration

import com.nimbusds.jose.jwk.RSAKey
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class RsaKeyConfiguration {

    @Bean
    fun jwtSignatureRsaKey(jwkProperties: RsaKeyProperties): RSAKey {
        val rsaKeyString = ClassPathResource(jwkProperties.keyPath).inputStream.reader(Charsets.UTF_8).readText()
        val rsaKey = RSAKey.parse(rsaKeyString)
        if (!rsaKey.isPrivate) {
            throw IllegalArgumentException("RSA Key is not private")
        }
        return rsaKey
    }

}