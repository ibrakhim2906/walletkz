package com.ibrakhim2906.walletkz.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotNull @Size(max = 255) String email,
        @NotNull String password
) {
}
