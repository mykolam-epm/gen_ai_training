package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the Azure OpenAI Async Client.
 * <p>
 * This configuration defines a bean that provides an asynchronous client for interacting with the Azure OpenAI Service.
 * It uses the Azure Key Credential for authentication and connects to a specified endpoint.
 */
@Configuration
public class OpenAIConfiguration {

  @Value("${client-azureopenai-key}") private String openAiKey;

  @Value("${client-azureopenai-endpoint}") private String openAiEndpoint;

  @Value("${openai-temperature:1.0}") private double temperature;

  /**
   * Creates an {@link OpenAIAsyncClient} bean for interacting with Azure OpenAI Service asynchronously.
   *
   * @return an instance of {@link OpenAIAsyncClient}
   */
  @Bean
  public OpenAIAsyncClient openAIAsyncClient() {
    return new OpenAIClientBuilder().credential(new AzureKeyCredential(openAiKey)).endpoint(openAiEndpoint)
      .buildAsyncClient();
  }

  /**
   * Creates a {@link Kernel} bean to manage AI services and plugins.
   *
   * @param chatCompletionService the {@link ChatCompletionService} for handling completions
   * @return an instance of {@link Kernel}
   */
  @Bean
  public Kernel kernel(ChatCompletionService chatCompletionService) {
    return Kernel.builder().withAIService(ChatCompletionService.class, chatCompletionService).build();
  }

  /**
   * Creates an {@link InvocationContext} bean with default prompt execution settings.
   *
   * @return an instance of {@link InvocationContext}
   */
  @Bean
  public InvocationContext invocationContext() {
    return InvocationContext.builder()
      .withPromptExecutionSettings(PromptExecutionSettings.builder().withTemperature(temperature).build()).build();
  }

}
