package com.arimar.gwent.contracts.auth.response;

import lombok.Data;

@Data
public class TokenResponse {

    private String accessToken;

    private String tokenType = "Bearer";

    private Long expiresIn;
}
