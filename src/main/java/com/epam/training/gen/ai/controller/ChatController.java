package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatInputDto;
import com.epam.training.gen.ai.dto.ChatOutputDto;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

  private final ChatCompletionService chatCompletionService;
  private final Kernel kernel;
  private final InvocationContext invocationContext;
  private final ChatHistory chatHistory;

  @PostMapping
  public ChatOutputDto chat(@RequestBody ChatInputDto input) {
    var prompt = input.getInput();
    log.info("Querying with input {}", prompt);
    chatHistory.addUserMessage(prompt);

    var response =
      chatCompletionService.getChatMessageContentsAsync(chatHistory, kernel, invocationContext).block().stream()
        .map(ChatMessageContent::getContent).collect(Collectors.joining("\n\n"));
    chatHistory.addAssistantMessage(response);
    return ChatOutputDto.builder().output(response).build();
  }
}
