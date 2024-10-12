package com.devshaks.delivery.customer.restaurants;

import com.devshaks.delivery.customer.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantClient {

    @Value("${application.config.restaurants-url}")
    private String restaurantsUrl; // Injecting the restaurant service base URL from the application configuration.

    private final RestTemplate restTemplate; // RestTemplate is used for making HTTP requests to external services.

    /**
     * Fetches the details of a single restaurant by its ID from the restaurant
     * service.
     *
     * @param restaurantId
     *            The ID of the restaurant to fetch.
     * @return A RestaurantResponse object containing restaurant details.
     * @throws BusinessException
     *             If the restaurant is not found (404 error), a BusinessException
     *             is thrown.
     */
    public RestaurantResponse getRestaurantsById(Integer restaurantId) {
        try {
            // Builds the URL for the restaurant service's /restaurants/{id} endpoint with
            // the restaurant ID.
            String url = UriComponentsBuilder.fromHttpUrl(restaurantsUrl)
                    .path("/restaurants/{restaurantId}")
                    .buildAndExpand(restaurantId)
                    .toUriString();
            // Sends a GET request to the restaurant service's /restaurants/{id} endpoint
            // and expects a RestaurantResponse.
            ResponseEntity<RestaurantResponse> response = restTemplate.getForEntity(url, RestaurantResponse.class);

            // returns the body of the response, which contains the restaurant details.
            return Optional.ofNullable(response.getBody())
                    .orElseThrow(() -> new BusinessException("Restaurant not found: " + restaurantId));
        } catch (HttpClientErrorException exception) {
            log.error("Error fetching restaurant details: {}", exception.getMessage());
            // Handles the case where the restaurant service returns a 4xx error, typically
            // a 404 (not found).
            throw new BusinessException("Restaurant not found: " + restaurantId);
        }
    }

    /**
     * Fetches the details of multiple restaurants based on a list of restaurant
     * IDs.
     *
     * @param restaurantIds
     *            A list of restaurant IDs to fetch.
     * @return A list of RestaurantResponse objects containing details of the
     *         restaurants.
     * @throws BusinessException
     *             If any restaurant is not found (404 error), a BusinessException
     *             is thrown.
     */
    public List<RestaurantResponse> getRestaurantsByIds(List<Integer> restaurantIds) {
        if (restaurantIds == null || restaurantIds.isEmpty()) {
            throw new IllegalArgumentException("Restaurant IDs cannot be null or empty");
        }
        try {
            String url = UriComponentsBuilder.fromHttpUrl(restaurantsUrl)
                    .path("/restaurants/search/by-ids")
                    .toUriString();

            // Creates an HTTP request entity that includes the list of restaurant IDs to be
            // sent in the request body.
            HttpEntity<List<Integer>> requestEntity = new HttpEntity<>(restaurantIds);
            // Defines a type reference for the expected response body, which is a list of
            // RestaurantResponse objects.
            ParameterizedTypeReference<List<RestaurantResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            // Sends a POST request to the restaurant service's /restaurants/by-ids endpoint
            // with the list of IDs.
            // The exchange method allows specifying the request method (POST) and response
            // type (List<RestaurantResponse>).
            ResponseEntity<List<RestaurantResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    responseType);

            // Returns the body of the response, which contains the list of restaurant
            // details.
            return Optional.ofNullable(response.getBody())
                    .orElseThrow(() -> new BusinessException("Restaurant not found: " + restaurantIds));
        } catch (HttpClientErrorException exception) {
            log.error("Error fetching restaurant details: {}", restaurantIds, exception);
            // Handles the case where the restaurant service returns a 4xx error, typically
            // a 404 (not found).
            throw new BusinessException("Restaurant not found: " + restaurantIds);
        }
    }
}
