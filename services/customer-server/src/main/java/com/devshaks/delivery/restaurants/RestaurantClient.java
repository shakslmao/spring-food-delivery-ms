package com.devshaks.delivery.restaurants;

import com.devshaks.delivery.customer.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantClient {
    @Value("${application.config.restaurants-url}")
    private String restaurantsUrl;
    private final RestTemplate restTemplate;

    public RestaurantResponse getRestaurantsById(Integer restaurantId) {
        try {
            ResponseEntity<RestaurantResponse> response = restTemplate.getForEntity(restaurantsUrl + "/restaurants/" + restaurantId, RestaurantResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException exception) {
            throw new BusinessException("Restaurant not found: " + restaurantId);
        }
    }

    public List<RestaurantResponse> getRestaurants(List<Integer> restaurantIds) {
        try {
            HttpEntity<List<Integer>> requestEntity = new HttpEntity<>(restaurantIds);
            ParameterizedTypeReference<List<RestaurantResponse>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<List<RestaurantResponse>> response = restTemplate.exchange(
                    restaurantsUrl + "/restaurants/by-ids",
                    HttpMethod.POST,
                    requestEntity,
                    responseType
            );
            return response.getBody();
        } catch (HttpClientErrorException exception) {
            throw new BusinessException("Restaurant not found: " + restaurantIds);
        }
    }
}
