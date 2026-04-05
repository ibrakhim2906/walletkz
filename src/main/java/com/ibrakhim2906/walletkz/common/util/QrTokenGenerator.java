package com.ibrakhim2906.walletkz.common.util;

import java.util.UUID;

public class QrTokenGenerator {

    // Used to generate unique qr token for transactions
    public static String generateToken() {

        return UUID.randomUUID().toString();
    }
}
