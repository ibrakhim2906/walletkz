package com.ibrakhim2906.walletkz.wallet;

import java.math.BigDecimal;

public record WalletResponse(
        Long id,
        CurrencyEnum currencyEnum,
        BigDecimal balance
) { }
