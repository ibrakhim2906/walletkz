package com.ibrakhim2906.walletkz.fx;

import com.ibrakhim2906.walletkz.wallet.CurrencyEnum;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class FxService {

    public BigDecimal getRate(CurrencyEnum from, CurrencyEnum to) {
        if (from==to) {
            return BigDecimal.ONE;
        }

        if (from == CurrencyEnum.USD && to == CurrencyEnum.KZT) {
            return new BigDecimal("515.00");
        }
        if (from == CurrencyEnum.KZT && to == CurrencyEnum.USD) {
            return new BigDecimal("0.00194175");
        }
        if (from == CurrencyEnum.EUR && to == CurrencyEnum.KZT) {
            return new BigDecimal("560.00");
        }
        if (from == CurrencyEnum.KZT && to == CurrencyEnum.EUR) {
            return new BigDecimal("0.00178571");
        }
        if (from == CurrencyEnum.USD && to == CurrencyEnum.EUR) {
            return new BigDecimal("0.92");
        }
        if (from == CurrencyEnum.EUR && to == CurrencyEnum.USD) {
            return new BigDecimal("1.09");
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rate not found");
    }

    public BigDecimal convert(BigDecimal amount, CurrencyEnum from, CurrencyEnum to) {
        BigDecimal rate = getRate(from, to);

        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
