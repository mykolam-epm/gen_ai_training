package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Semantic Kernel components.
 * <p>
 * This configuration provides several beans necessary for the interaction with Azure OpenAI services and the creation
 * of kernel plugins. It defines beans for chat completion services, kernel plugins, kernel instance, invocation
 * context, and prompt execution settings.
 */
@Configuration
public class SemanticKernelConfiguration {

  /**
   * Creates a {@link ChatCompletionService} bean for handling chat completions using Azure OpenAI.
   *
   * @param deploymentOrModelName the Azure OpenAI deployment or model name
   * @param openAIAsyncClient the {@link OpenAIAsyncClient} to communicate with Azure OpenAI
   * @return an instance of {@link ChatCompletionService}
   */
  @Bean
  public ChatCompletionService chatCompletionService(
    @Value("${client-azureopenai-deployment-name}") String deploymentOrModelName, OpenAIAsyncClient openAIAsyncClient) {
    return OpenAIChatCompletion.builder().withModelId(deploymentOrModelName).withOpenAIAsyncClient(openAIAsyncClient)
      .build();
  }
}
