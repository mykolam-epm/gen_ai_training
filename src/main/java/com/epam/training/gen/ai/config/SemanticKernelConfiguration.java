package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.plugin.DemoSemanticKernelPlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
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

  @Bean
  public ChatHistory chatHistory() {
    return new ChatHistory();
  }

  /**
   * Creates a {@link KernelPlugin} bean using a simple plugin.
   *
   * @return an instance of {@link KernelPlugin}
   */
  @Bean
  public KernelPlugin kernelPlugin() {
    return KernelPluginFactory.createFromObject(
      new DemoSemanticKernelPlugin(), "WeatherPlugin");
  }

  /**
   * Creates a {@link Kernel} bean to manage AI services and plugins.
   *
   * @param chatCompletionService the {@link ChatCompletionService} for handling completions
   * @return an instance of {@link Kernel}
   */
  @Bean
  public Kernel kernel(ChatCompletionService chatCompletionService, KernelPlugin kernelPlugin) {
    return Kernel.builder().withAIService(ChatCompletionService.class, chatCompletionService)
      .withPlugin(kernelPlugin).build();
  }
}
