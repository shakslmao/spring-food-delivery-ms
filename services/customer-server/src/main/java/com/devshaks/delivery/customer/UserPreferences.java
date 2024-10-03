package com.devshaks.delivery.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferences {

    @Field("language")
    private String language;

    @Field("notifications")
    private boolean notifications;
}
