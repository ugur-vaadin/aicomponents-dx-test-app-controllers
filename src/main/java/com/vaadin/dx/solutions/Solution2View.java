package com.vaadin.dx.solutions;

import javax.sql.DataSource;

import com.vaadin.dx.LLMHelper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;

import com.vaadin.flow.component.ai.chart.ChartAIController;
import com.vaadin.flow.component.ai.orchestrator.AIOrchestrator;
import com.vaadin.flow.component.ai.provider.SpringAILLMProvider;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.UploadDropZone;
import com.vaadin.flow.component.upload.UploadFileList;
import com.vaadin.flow.component.upload.UploadManager;
import com.vaadin.flow.router.Route;

/**
 * Solution 2: Chart populated from natural language.
 */
@Route("solution2")
public class Solution2View extends UploadDropZone {

    public Solution2View(DataSource dataSource) {
        // --- Task 2: Chart ---
        var db = new Solution1View.H2DatabaseProvider(dataSource);

        var chart = new Chart();
        chart.setSizeFull();
        var chartController = new ChartAIController(chart, db);

        var systemPrompt = ChartAIController.getSystemPrompt();

        // LLM provider
        var provider = new SpringAILLMProvider(LLMHelper.getChatModel());

        // Chat UI
        var chatPanel = createChatPanel();

        var contentPanel = new VerticalLayout(new Span("Content Area"), chart);
        contentPanel.setHeightFull();
        contentPanel.setPadding(true);

        AIOrchestrator.builder(provider, systemPrompt)
                .withMessageList(chatPanel.messageList).withInput(chatPanel.messageInput)
                .withFileReceiver(chatPanel.uploadManager)
                .withController(chartController).build();

        initMainLayout(chatPanel, contentPanel);
    }

    private void initMainLayout(Component chatPanel, Component contentPanel) {
        setSizeFull();
        var mainLayout = new HorizontalLayout(chatPanel, contentPanel);
        mainLayout.setSizeFull();
        mainLayout.expand(contentPanel);
        setContent(mainLayout);
    }

    private ChatPanel createChatPanel() {
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
        return new ChatPanel(fileList, messageList, messageInput, uploadManager);
    }

    private static class ChatPanel extends VerticalLayout {
        private final UploadFileList fileList;
        private final MessageList messageList;
        private final MessageInput messageInput;
        private final UploadManager uploadManager;

        ChatPanel(UploadFileList fileList, MessageList messageList, MessageInput messageInput, UploadManager uploadManager) {
            this.fileList = fileList;
            this.messageList = messageList;
            this.messageInput = messageInput;
            this.uploadManager = uploadManager;
            add(fileList, messageList, messageInput);
            setWidth("600px");
            setHeightFull();
            setPadding(false);
            setSpacing(false);
            expand(messageList);
        }

        UploadFileList getFileList() {
            return fileList;
        }

        MessageList getMessageList() {
            return messageList;
        }

        MessageInput getMessageInput() {
            return messageInput;
        }

        UploadManager getUploadManager() {
            return uploadManager;
        }
    }
}
