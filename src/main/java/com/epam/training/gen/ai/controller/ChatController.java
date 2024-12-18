package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.client.EpamDialClient;
import com.epam.training.gen.ai.dto.ChatInputDto;
import com.epam.training.gen.ai.dto.ChatOutputDto;
import com.epam.training.gen.ai.dto.EpamDialDeployments.EpamDialDeployment;
import com.epam.training.gen.ai.service.EmbeddingsService;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import io.qdrant.client.grpc.Points.ScoredPoint;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

  private final ChatCompletionService chatCompletionService;
  private final Kernel kernel;
  private final ChatHistory chatHistory;
  private final EpamDialClient dialClient;
  private final EmbeddingsService embeddingsService;

  @PostMapping
  public ChatOutputDto chat(@RequestBody ChatInputDto input) {
    var prompt = input.getInput();
    log.info("Querying with input {}", prompt);
    chatHistory.addSystemMessage(
      "You are a helpful assistant that uses WeatherPlugin getWeather function to provide weather for given latitude+longitude");
    chatHistory.addUserMessage(prompt);

    var invocationContext = InvocationContext.builder().withPromptExecutionSettings(
        PromptExecutionSettings.builder().withTemperature(input.getTemperature()).withModelId(input.getModel()).build())
      .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
      .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY).build();

    var response =
      chatCompletionService.getChatMessageContentsAsync(chatHistory, kernel, invocationContext).block().stream()
        .map(ChatMessageContent::getContent).collect(Collectors.joining("\n\n"));
    chatHistory.addAssistantMessage(response);
    return ChatOutputDto.builder().output(response).build();
  }

  @PostMapping("embeddings")
  public void storeEmbedding(@RequestBody ChatInputDto input) {
    embeddingsService.saveEmbeddings(input.getInput());
  }

  @PostMapping("embeddings/search")
  public List<String> findEmbedding(@RequestBody ChatInputDto input) {
    return embeddingsService.findEmbeddings(input.getInput());
  }

  @GetMapping("models")
  public List<String> listAvailableAiModels() {
    return dialClient.listDeployments().getData().stream().map(EpamDialDeployment::getId).toList();
  }
}
