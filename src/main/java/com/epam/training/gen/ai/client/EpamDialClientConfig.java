package com.epam.training.gen.ai.client;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class EpamDialClientConfig {

  @Value("${client-azureopenai-key}") private String dialApiKey;

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> requestTemplate.header("Api-Key", dialApiKey);
  }
}
