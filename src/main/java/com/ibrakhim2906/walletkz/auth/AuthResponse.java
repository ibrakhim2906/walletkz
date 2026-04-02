package com.ibrakhim2906.walletkz.auth;

import jakarta.validation.constraints.NotNull;

public record AuthResponse(
        @NotNull String token
) { }
