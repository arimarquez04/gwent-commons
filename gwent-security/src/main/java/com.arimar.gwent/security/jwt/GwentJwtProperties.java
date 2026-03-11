package com.arimar.gwent.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("gwent.security.jwt")
public record GwentJwtProperties(
        String jwksUri
) {}
