package com.arimar.gwent.communication.config;

import com.arimar.gwent.communication.error.RemoteServiceErrorMapper;
import com.arimar.gwent.communication.error.RemoteServiceErrorMapperImpl;
import com.arimar.gwent.communication.invoker.GenericRestInvoker;
import com.arimar.gwent.communication.invoker.GenericRestInvokerImpl;
import com.arimar.gwent.communication.security.AuthForwardingInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
public class CommonsCommunicationAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public AuthForwardingInterceptor authForwardingInterceptor() {
        return new AuthForwardingInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public RestClient restClient(RestClient.Builder builder, AuthForwardingInterceptor authInterceptor) {
        return builder
                .requestInterceptor(authInterceptor)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public GenericRestInvoker genericRestInvoker(RestClient restClient, ObjectMapper objectMapper) {
        return new GenericRestInvokerImpl(restClient, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public RemoteServiceErrorMapper remoteServiceErrorMapper() {
        return new RemoteServiceErrorMapperImpl();
    }
}
