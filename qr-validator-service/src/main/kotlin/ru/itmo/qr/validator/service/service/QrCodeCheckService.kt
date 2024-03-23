package ru.itmo.qr.validator.service.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile
import ru.itmo.qr.validator.service.model.UserCertificateInfo
import ru.itmo.qr.validator.service.model.exception.CertificateNotFoundException
import ru.itmo.qr.validator.service.model.exception.KachalkaRequestException
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

@Service
class QrCodeCheckService(
    private val objectMapper: ObjectMapper,
    private val kachalkaRestTemplate: RestTemplate,
) {

    fun validateQrCode(qrCodeImage: MultipartFile): Boolean {
        val bufferedImage = ImageIO.read(ByteArrayInputStream(qrCodeImage.bytes))
        val result = MultiFormatReader().decode(BinaryBitmap(HybridBinarizer(BufferedImageLuminanceSource(bufferedImage))))
        val userCertificateInfo = objectMapper.readValue<UserCertificateInfo>(result.text)
        val httpEntity = HttpEntity<UserCertificateInfo>(userCertificateInfo)
        try {
            val statusCode = kachalkaRestTemplate.postForEntity("/v1/validator", httpEntity, UserCertificateInfo::class.java)
                .statusCode
            return statusCode == HttpStatus.OK
        } catch (e: HttpClientErrorException) {
            if (e.statusCode == HttpStatus.NOT_FOUND) {
                throw CertificateNotFoundException("Certificate not found")
            }
            throw KachalkaRequestException("Request to kachalka failed, statusCode = ${e.statusCode}, message = ${e.message}")
        }
    }

}