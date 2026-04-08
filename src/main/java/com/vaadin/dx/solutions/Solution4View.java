package com.vaadin.dx.solutions;

import java.util.List;

import javax.sql.DataSource;

import com.vaadin.dx.ViewHelper;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import com.vaadin.flow.component.ai.orchestrator.AIController;
import com.vaadin.flow.component.ai.orchestrator.AIOrchestrator;
import com.vaadin.flow.component.ai.provider.LLMProvider;
import com.vaadin.flow.component.ai.provider.SpringAILLMProvider;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Solution 4: Custom AIController that changes background color.
 */
@Route("solution4")
public class Solution4View extends VerticalLayout {

    public Solution4View(DataSource dataSource) {
        // LLM provider
        var openAiApi = OpenAiApi.builder()
                .apiKey(System.getenv("OPENAI_API_KEY")).build();
        var chatModel = OpenAiChatModel.builder().openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4o-mini").build())
                .build();
        var provider = new SpringAILLMProvider(chatModel);

        // Custom controller
        var colorController = new BackgroundColorController(this);

        // Chat UI
        var messageList = new MessageList();
        messageList.setWidthFull();
        messageList.setMaxHeight("300px");
        var messageInput = new MessageInput();
        messageInput.setWidthFull();

        var systemPrompt = "You can change the background color of the "
                + "page. When the user asks, use the "
                + "set_background_color tool.";

        // Orchestrator
        AIOrchestrator.builder(provider, systemPrompt)
                .withMessageList(messageList).withInput(messageInput)
                .withController(colorController).build();

        add(messageList, messageInput);
        setSizeFull();
        setPadding(true);
    }

    /**
     * Custom AIController that provides a tool to change the view's
     * background color.
     */
    static class BackgroundColorController implements AIController {

        private final VerticalLayout view;

        BackgroundColorController(VerticalLayout view) {
            this.view = view;
        }

        @Override
        public List<LLMProvider.ToolSpec> getTools() {
            return List.of(new LLMProvider.ToolSpec() {
                @Override
                public String getName() {
                    return "set_background_color";
                }

                @Override
                public String getDescription() {
                    return "Sets the background color of the page. "
                            + "Accepts any valid CSS color value.";
                }

                @Override
                public String getParametersSchema() {
                    return """
                            {
                              "type": "object",
                              "properties": {
                                "color": {
                                  "type": "string",
                                  "description": "CSS color value (e.g., 'blue', '#ff0000', 'rgb(0,128,0)')"
                                }
                              },
                              "required": ["color"]
                            }""";
                }

                @Override
                public String execute(String arguments) {
                    try {
                        var color = ViewHelper.getStringProperty(
                                arguments, "color");
                        ViewHelper.setBackgroundColor(view, color);
                        return "Background color set to " + color;
                    } catch (Exception e) {
                        return "Error: " + e.getMessage();
                    }
                }
            });
        }
    }
}
