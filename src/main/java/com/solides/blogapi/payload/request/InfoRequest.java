package com.solides.blogapi.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InfoRequest {


    @NotBlank
    private String city;

    @NotBlank
    private String zipcode;

    private String phone;

    private String lat;

    private String lng;
}
