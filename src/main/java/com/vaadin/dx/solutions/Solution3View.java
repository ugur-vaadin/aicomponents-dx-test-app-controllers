package com.vaadin.dx.solutions;

import javax.sql.DataSource;

import com.vaadin.flow.component.html.Span;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import com.vaadin.dx.StateStorage;
import com.vaadin.flow.component.ai.chart.ChartAIController;
import com.vaadin.flow.component.ai.chart.ChartState;
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
 * Solution 3: Chart state persistence across page refresh.
 */
@Route("solution3")
public class Solution3View extends UploadDropZone {

    public Solution3View(DataSource dataSource) {
        setSizeFull();

        // --- Task 3: State persistence ---
        var db = new Solution1View.H2DatabaseProvider(dataSource);

        var chart = new Chart();
        chart.setSizeFull();
        var chartController = new ChartAIController(chart, db);

        // Restore state
        var savedState = (ChartState) StateStorage
                .retrieve("chart-state");
        if (savedState != null) {
            chartController.restoreState(savedState);
        }

        // Save state on every change
        chartController.addStateChangeListener(state ->
                StateStorage.persist("chart-state", state));

        var systemPrompt = ChartAIController.getSystemPrompt();

        // --- UI setup ---
        var openAiApi = OpenAiApi.builder()
                .apiKey(System.getenv("OPENAI_API_KEY")).build();
        var chatModel = OpenAiChatModel.builder().openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-5.4-mini").build())
                .build();
        var provider = new SpringAILLMProvider(chatModel);

        var messageList = new MessageList();
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

        var contentPanel = new VerticalLayout(new Span("Content Area"), chart);
        contentPanel.setHeightFull();
        contentPanel.setPadding(true);

        AIOrchestrator.builder(provider, systemPrompt)
                .withMessageList(messageList).withInput(messageInput)
                .withFileReceiver(uploadManager)
                .withController(chartController).build();

        var mainLayout = new HorizontalLayout(chatPanel, contentPanel);
        mainLayout.setSizeFull();
        mainLayout.expand(contentPanel);
        setContent(mainLayout);
    }
}
