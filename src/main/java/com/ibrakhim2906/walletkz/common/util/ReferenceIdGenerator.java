package com.ibrakhim2906.walletkz.common.util;

import java.util.UUID;

// Used to generate unique referenceId for transactions
public class ReferenceIdGenerator {
    public static String generateReference() {

        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "TXN-"+random;

    }
}
