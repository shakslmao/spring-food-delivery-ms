package com.devshaks.delivery.handlers;

import com.devshaks.delivery.exceptions.BusinessException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, feign.Response response) {
        if (response.status() == 400) {
            return new BusinessException("Bad Request: Cannot Purchase Products");
        } else if (response.status() == 404) {
            return new BusinessException("Product not found");
        } else if (response.status() == 500) {
            return new BusinessException("Internal Server Error at Product Service");
        }
        return defaultDecoder.decode(methodKey, response);
    }
}
