package com.vaadin.dx.solutions;

import java.util.List;

import javax.sql.DataSource;

import com.vaadin.dx.LLMHelper;
import com.vaadin.dx.ViewHelper;
import com.vaadin.flow.component.html.Span;

import com.vaadin.flow.component.ai.orchestrator.AIController;
import com.vaadin.flow.component.ai.orchestrator.AIOrchestrator;
import com.vaadin.flow.component.ai.provider.LLMProvider;
import com.vaadin.flow.component.ai.provider.SpringAILLMProvider;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.UploadDropZone;
import com.vaadin.flow.component.upload.UploadFileList;
import com.vaadin.flow.component.upload.UploadManager;
import com.vaadin.flow.router.Route;

/**
 * Solution 4: Custom AIController that changes background color.
 */
@Route("solution4")
public class Solution4View extends UploadDropZone {

    public Solution4View(DataSource dataSource) {
        setSizeFull();

        // --- Task 4: Custom controller ---
        var colorController = new BackgroundColorController(this);

        var systemPrompt = "You can change the background color of the "
                + "page. When the user asks, use the "
                + "set_background_color tool.";

        // LLM provider
        var provider = new SpringAILLMProvider(LLMHelper.getChatModel());

        // --- UI setup ---
        var messageList = new MessageList();
        messageList.setMarkdown(true);
        messageList.setWidthFull();
        messageList.setHeightFull();
        var messageInput = new MessageInput();
        messageInput.setWidthFull();
        var uploadManager = new UploadManager(this);
        setUploadManager(uploadManager);
        var fileList = new UploadFileList(uploadManager);
        fileList.setWidthFull();

        var chatPanel = new VerticalLayout(fileList, messageList,
                messageInput);
        chatPanel.setWidth("600px");
        chatPanel.setHeightFull();
        chatPanel.setPadding(false);
        chatPanel.setSpacing(false);
        chatPanel.expand(messageList);

        var contentPanel = new VerticalLayout(new Span("Content Area"));
        contentPanel.setHeightFull();
        contentPanel.setPadding(true);

        AIOrchestrator.builder(provider, systemPrompt)
                .withMessageList(messageList).withInput(messageInput)
                .withFileReceiver(uploadManager)
                .withController(colorController).build();

        var mainLayout = new HorizontalLayout(chatPanel, contentPanel);
        mainLayout.setSizeFull();
        mainLayout.expand(contentPanel);
        setContent(mainLayout);
    }

    /**
     * Custom AIController that provides a tool to change the view's
     * background color.
     */
    static class BackgroundColorController implements AIController {

        private final UploadDropZone view;

        BackgroundColorController(UploadDropZone view) {
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
