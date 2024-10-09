package com.devshaks.delivery.handlers;

import java.util.Map;

public record ErrorResponse (
    Map<String, String> errors
){

}
