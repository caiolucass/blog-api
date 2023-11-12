package com.solides.blogapi.payload.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType;

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
