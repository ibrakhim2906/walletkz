package com.ibrakhim2906.walletkz.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class QrImageGenerator {

    public static String generateBase64Qr(String text) {

        try {
            int width = 250;
            int height = 250;

            QRCodeWriter writer = new QRCodeWriter();

            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE,
                    width, height);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
                }
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream);

            return Base64.toBase64String(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("The QR image failed to generate");
        }

    }
}
