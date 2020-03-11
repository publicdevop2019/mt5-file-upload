package com.hw.shared;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class OAuthRestTemplateConfig {

    @Autowired
    OutgoingReqInterceptor requestInterceptor;

    @Bean
    public RestTemplate getRestTemplate(HttpComponentsClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(requestInterceptor));
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory getHttpComponentsClientHttpRequestFactory() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

}