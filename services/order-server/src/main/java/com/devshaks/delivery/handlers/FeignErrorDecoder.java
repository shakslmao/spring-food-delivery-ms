package com.devshaks.delivery.handlers;

import com.devshaks.delivery.exceptions.BusinessException;

import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, feign.Response response) {
        if (response.status() == 400) {
            return new BusinessException("Bad Request");
        } else if (response.status() == 404) {
            return new BusinessException("Customer not found");
        }
        return new Exception(response.reason());
    }
}
