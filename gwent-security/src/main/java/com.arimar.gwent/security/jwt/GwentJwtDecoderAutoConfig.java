package com.arimar.gwent.security.jwt;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@AutoConfiguration
@EnableConfigurationProperties(GwentJwtProperties.class)
@ConditionalOnProperty("gwent.security.jwt.jwks-uri")
public class GwentJwtDecoderAutoConfig {

    @Bean
    @ConditionalOnMissingBean(JwtDecoder.class)
    public JwtDecoder jwtDecoder(GwentJwtProperties props) {
        return NimbusJwtDecoder.withJwkSetUri(props.jwksUri()).build();
    }
}
