package ru.itmo.lab1.service.qr

import com.google.zxing.BarcodeFormat
import com.google.zxing.Writer
import com.google.zxing.WriterException
import com.google.zxing.client.j2se.MatrixToImageConfig
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.reactor.mono
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink
import java.io.ByteArrayOutputStream
import java.io.IOException

@Service
class QrCodeGeneratorService {

    fun generateQrCode(data: String): Mono<ByteArray> = generateByteArray(data, QRCodeWriter())

    private fun generateByteArray(
        barcodeText: String,
        writer: Writer,
    ): Mono<ByteArray> = mono {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val bitMatrix: BitMatrix = writer.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200)
        MatrixToImageWriter.writeToStream(
            bitMatrix,
            MediaType.IMAGE_PNG.subtype,
            byteArrayOutputStream,
            MatrixToImageConfig()
        )
        val byteArray = byteArrayOutputStream.toByteArray()
        byteArray
    }
}