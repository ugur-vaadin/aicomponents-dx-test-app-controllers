package com.vaadin.dx;

import javax.sql.DataSource;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import com.vaadin.flow.component.ai.orchestrator.AIOrchestrator;
import com.vaadin.flow.component.ai.provider.SpringAILLMProvider;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.UploadDropZone;
import com.vaadin.flow.component.upload.UploadFileList;
import com.vaadin.flow.component.upload.UploadManager;
import com.vaadin.flow.router.Route;

/**
 * DX Test — implement each task here.
 * <p>
 * The application has an H2 in-memory database with these tables:
 * - employees(id, name, department, salary, hire_date)
 * - sales(id, product, category, region, amount, quantity, sale_date)
 * - order_hdr(order_id, cust_name, order_dt, status)
 * - order_dtl(id, order_id, product, qty, unit_px)
 * - temperatures(id, city, month, avg_temp, min_temp, max_temp)
 * <p>
 * You can inject the DataSource to access the database.
 * <p>
 * For running SQL queries, use the {@link DatabaseHelper} helper:
 * - {@code DatabaseHelper.query(dataSource, "SELECT * FROM employees")}
 * <p>
 * For state persistence, use the {@link StateStorage} helper:
 * - {@code StateStorage.persist("myKey", data)} to save
 * - {@code StateStorage.retrieve("myKey")} to load
 * <p>
 * For view utilities, use the {@link ViewHelper} helper:
 * - {@code ViewHelper.getStringProperty(json, "propertyName")} to extract a value from JSON
 * - {@code ViewHelper.setBackgroundColor(component, "blue")} to set a CSS background color
 */
@Route("")
public class TaskView extends UploadDropZone {

    public TaskView(DataSource dataSource) {
        // Layout
        var layout = new VerticalLayout();
        layout.setWidth("600px");
        layout.setHeightFull();
        layout.setPadding(true);
        setContent(layout);

        // LLM provider
        var openAiApi = OpenAiApi.builder()
                .apiKey(System.getenv("OPENAI_API_KEY")).build();
        var chatModel = OpenAiChatModel.builder().openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-5.4-mini").build())
                .build();
        var provider = new SpringAILLMProvider(chatModel);

        // Chat UI
        var messageList = new MessageList();
        messageList.setWidthFull();
        messageList.setMaxHeight("250px");
        var messageInput = new MessageInput();
        messageInput.setWidthFull();
        var uploadManager = new UploadManager(this);
        setUploadManager(uploadManager);
        var fileList = new UploadFileList(uploadManager);
        fileList.setWidthFull();

        // Orchestrator
        String systemPrompt = null;
        AIOrchestrator.builder(provider, systemPrompt)
                .withMessageList(messageList)
                .withInput(messageInput)
                .withFileReceiver(uploadManager)
                .build();

        layout.add(fileList, messageList, messageInput);
    }
}
