package com.ibrakhim2906.walletkz.common.exception;

import java.time.LocalDateTime;

public record ErrorTemplate(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) { }
