package com.vaadin.dx.solutions;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.vaadin.dx.DatabaseHelper;

import com.vaadin.flow.component.html.Span;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import com.vaadin.flow.component.ai.grid.GridAIController;
import com.vaadin.flow.component.ai.orchestrator.AIOrchestrator;
import com.vaadin.flow.component.ai.provider.DatabaseProvider;
import com.vaadin.flow.component.ai.provider.SpringAILLMProvider;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.UploadDropZone;
import com.vaadin.flow.component.upload.UploadFileList;
import com.vaadin.flow.component.upload.UploadManager;
import com.vaadin.flow.router.Route;

/**
 * Solution 1: Grid populated from natural language.
 */
@Route("solution1")
public class Solution1View extends UploadDropZone {

    public Solution1View(DataSource dataSource) {
        setSizeFull();

        // --- Task 1: DatabaseProvider + Grid ---
        var db = new H2DatabaseProvider(dataSource);

        var grid = new Grid<Map<String, Object>>();
        grid.setSizeFull();
        var gridController = new GridAIController(grid, db);

        var systemPrompt = GridAIController.getSystemPrompt();

        // --- UI setup ---
        var openAiApi = OpenAiApi.builder()
                .apiKey(System.getenv("OPENAI_API_KEY")).build();
        var chatModel = OpenAiChatModel.builder().openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-5.4-mini").build())
                .build();
        var provider = new SpringAILLMProvider(chatModel);

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

        var contentPanel = new VerticalLayout(new Span("Content Area"), grid);
        contentPanel.setHeightFull();
        contentPanel.setPadding(true);

        AIOrchestrator.builder(provider, systemPrompt)
                .withMessageList(messageList).withInput(messageInput)
                .withFileReceiver(uploadManager)
                .withController(gridController).build();

        var mainLayout = new HorizontalLayout(chatPanel, contentPanel);
        mainLayout.setSizeFull();
        mainLayout.expand(contentPanel);
        setContent(mainLayout);
    }

    /**
     * DatabaseProvider backed by the H2 DataSource.
     */
    static class H2DatabaseProvider implements DatabaseProvider {

        private static final String SCHEMA = """
                Tables:
                - employees(id INT, name VARCHAR, department VARCHAR, salary DECIMAL, hire_date DATE)
                - sales(id INT, product VARCHAR, category VARCHAR, region VARCHAR, amount DECIMAL, quantity INT, sale_date DATE)
                - order_hdr(order_id INT, cust_name VARCHAR, order_dt DATE, status VARCHAR)
                - order_dtl(id INT, order_id INT, product VARCHAR, qty INT, unit_px DECIMAL)
                - temperatures(id INT, city VARCHAR, month VARCHAR, avg_temp DECIMAL, min_temp DECIMAL, max_temp DECIMAL)
                """;

        private final DataSource dataSource;

        H2DatabaseProvider(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public String getSchema() {
            return SCHEMA;
        }

        @Override
        public List<Map<String, Object>> executeQuery(String sql) {
            return DatabaseHelper.query(dataSource, sql);
        }
    }
}
