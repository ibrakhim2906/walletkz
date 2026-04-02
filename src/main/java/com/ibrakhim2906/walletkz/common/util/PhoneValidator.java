package com.ibrakhim2906.walletkz.common.util;

import com.google.i18n.phonenumbers.*;
import org.springframework.stereotype.Component;

@Component
public class PhoneValidator {

    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public static boolean isValid(String phone) {
        try {
            Phonenumber.PhoneNumber number =
                    phoneUtil.parse(phone, "KZ"); // default country

            return phoneUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            return false;
        }
    }
}