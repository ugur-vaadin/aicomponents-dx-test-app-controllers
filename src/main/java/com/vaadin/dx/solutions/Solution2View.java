package com.vaadin.dx.solutions;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import com.vaadin.flow.component.ai.grid.GridAIController;
import com.vaadin.flow.component.ai.orchestrator.AIOrchestrator;
import com.vaadin.flow.component.ai.provider.SpringAILLMProvider;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Solution 2: Grid populated from natural language.
 */
@Route("solution2")
public class Solution2View extends VerticalLayout {

    public Solution2View(DataSource dataSource) {
        var db = new Solution1View.H2DatabaseProvider(dataSource);

        // LLM provider
        var openAiApi = OpenAiApi.builder()
                .apiKey(System.getenv("OPENAI_API_KEY")).build();
        var chatModel = OpenAiChatModel.builder().openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4o-mini").build())
                .build();
        var provider = new SpringAILLMProvider(chatModel);

        // Grid + controller
        var grid = new Grid<Map<String, Object>>();
        grid.setHeight("400px");
        grid.setWidthFull();
        var gridController = new GridAIController(grid, db);

        var systemPrompt = GridAIController.getSystemPrompt();

        // Chat UI
        var messageList = new MessageList();
        messageList.setWidthFull();
        messageList.setMaxHeight("250px");
        var messageInput = new MessageInput();
        messageInput.setWidthFull();

        // Orchestrator
        var orchestrator = AIOrchestrator.builder(provider, systemPrompt)
                .withMessageList(messageList).withInput(messageInput)
                .withController(gridController).build();

        add(grid, messageList, messageInput);
        setSizeFull();
        setPadding(true);
    }
}
