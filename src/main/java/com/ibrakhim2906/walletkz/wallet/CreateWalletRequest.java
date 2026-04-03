package com.ibrakhim2906.walletkz.wallet;

import jakarta.validation.constraints.NotNull;

public record CreateWalletRequest(
        @NotNull CurrencyEnum currency
) { }
