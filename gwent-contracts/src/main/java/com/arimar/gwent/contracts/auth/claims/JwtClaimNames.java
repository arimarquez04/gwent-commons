package com.arimar.gwent.contracts.auth.claims;

import lombok.Getter;

@Getter
public class JwtClaimNames {
    // Custom / domain claims
    public static final String USER_ID = "userId";
    public static final String USERNAME  = "username";
    public static final String GAME_ID   = "gameId";
    public static final String TAG       = "tag";

    // Standard JWT registered claim names (if you need constants)
    public static final String SUBJECT   = "sub";
    public static final String ISSUER    = "iss";
    public static final String AUDIENCE  = "aud";
    public static final String ISSUED_AT = "iat";
    public static final String EXPIRES_AT= "exp";
    public static final String JWT_ID    = "jti";

}
