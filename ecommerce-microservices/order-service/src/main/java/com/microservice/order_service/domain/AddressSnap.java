package com.microservice.order_service.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressSnap {
    private String fullName;
    private String streetNo;
    private String houseNo;
    private String area;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phoneNo;
}
