package com.devshaks.delivery.customer.handlers;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {
}
