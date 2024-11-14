package com.devshaks.delivery.order;

import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    public Integer createOrder(@Valid OrderRequest orderRequest) {
        return null;
    }

}
