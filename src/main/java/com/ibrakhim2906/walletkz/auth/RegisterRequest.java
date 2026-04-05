package com.ibrakhim2906.walletkz.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email @NotNull @Size(max = 255) String email,
        @NotNull @Size(max = 30) String phone,
        @NotNull @Size(min =8, max = 255) String password
) { }
