package com.devshaks.delivery.cuisine;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CuisineTypesRepository extends JpaRepository<CuisineTypes, Integer> {
    List<CuisineTypes> findCuisineTypesByRestaurantId(Integer restaurantId);
}
