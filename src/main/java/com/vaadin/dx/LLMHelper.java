package com.vaadin.dx;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

public class LLMHelper {
    public static ChatModel getChatModel() {
        var anthropicApiKey = System.getenv("ANTHROPIC_API_KEY");
        var openaiApiKey = System.getenv("OPENAI_API_KEY");
        if (anthropicApiKey != null && openaiApiKey != null) {
            throw new RuntimeException("Only one of Anthropic API key and OpenAI API key must be set");
        }
        if (anthropicApiKey == null && openaiApiKey == null) {
            throw new RuntimeException("Anthropic API key or OpenAI API key must be set");
        }
        var modelName = "gpt-5.4-mini";
        if  (anthropicApiKey != null) {
            var anthropicChatOptions = AnthropicChatOptions.builder()
                    .model(modelName).apiKey(anthropicApiKey).build();
            return AnthropicChatModel.builder().options(anthropicChatOptions).build();
        }
        var openAiApi = OpenAiApi.builder().apiKey(openaiApiKey).build();
        return OpenAiChatModel.builder().openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(modelName).build())
                .build();
    }
}
