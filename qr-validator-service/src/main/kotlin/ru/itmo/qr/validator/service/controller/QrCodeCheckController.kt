package ru.itmo.qr.validator.service.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.itmo.qr.validator.service.service.QrCodeCheckService


@RestController
class QrCodeCheckController(
    private val qrCodeCheckService: QrCodeCheckService,
) {

    @PostMapping("/validate")
    fun checkQrCode(@RequestParam("qrCode") qrCodeImage: MultipartFile): Boolean = qrCodeCheckService.validateQrCode(qrCodeImage)

}