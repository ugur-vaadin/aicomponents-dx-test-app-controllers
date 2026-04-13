package com.vaadin.dx;

import javax.sql.DataSource;

import com.vaadin.flow.component.html.Span;

import com.vaadin.flow.component.ai.orchestrator.AIOrchestrator;
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
 * DX Test — implement each task here.
 * <p>
 * TASKS:
 * <ol>
 *  <li>Showing data in a grid using natural language:</li>
 *  Add a Grid and a chat input to the view. Set things up so that the user
 *      can ask questions about the database in natural language and the grid
 *      populates with the results. For example, asking "Show me all employees
 *      with their name, department, and salary" should fill the grid.
 *  <ul>
 *  <li>
 *  The application has an H2 in-memory database with these tables:
 *  <ul>
 *      <li>employees(id, name, department, salary, hire_date)</li>
 *      <li>sales(id, product, category, region, amount, quantity, sale_date)</li>
 *      <li>order_hdr(order_id, cust_name, order_dt, status)</li>
 *      <li>order_dtl(id, order_id, product, qty, unit_px)</li>
 *      <li>temperatures(id, city, month, avg_temp, min_temp, max_temp)</li>
 *  </ul>
 *  </li>
 *  <li>You can inject the DataSource to access the database.</li>
 *  <li>
 *  For running SQL queries, use the {@link DatabaseHelper} helper:
 *      {@code DatabaseHelper.query(dataSource, "SELECT * FROM employees")}
 *  </li>
 *  </ul>
 *  <li>Visualizing data in a chart</li>
 *  Replace the grid with a Chart component. Set things up so that the user
 *      can ask for chart visualizations in natural language. For example,
 *      asking "Show me a bar chart of total sales amount by product" should
 *      render a chart.
 *  <li>Persisting state</li>
 *  Make it so that the chart survives a page refresh. When the user returns
 *      to the page, the same chart should appear without asking the assistant
 *      again.
 *  For state persistence, use the {@link StateStorage} helper:
 *  <ul>
 *      <li>{@code StateStorage.persist("myKey", data)} to save</li>
 *      <li>{@code StateStorage.retrieve("myKey")} to load</li>
 *  </ul>
 *  <li>Giving the AI a custom ability</li>
 *  Give the AI the ability to change the view's background color to a CSS
 *      color value (e.g., "blue", "#ff0000", "rgb(0,128,0)"). When the user
 *      says something like "make the background blue", the background color
 *      should change.
 *  <p>
 *  For view utilities, use the {@link ViewHelper} helper:
 *  <ul>
 *      <li>{@code ViewHelper.getStringProperty(json, "propertyName")} to
 *          extract a value from JSON</li>
 *      <li>{@code ViewHelper.setBackgroundColor(component, "blue")} to
 *          set a CSS background color</li>
 *  </ul>
 * </ol>
 */
@Route("")
public class TaskView extends UploadDropZone {

    public TaskView(DataSource dataSource) {
        setSizeFull();

        // LLM provider
        var provider = new SpringAILLMProvider(LLMHelper.getChatModel());

        // Chat UI
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

        // Chat panel (left side)
        var chatPanel = new VerticalLayout(fileList, messageList,
                messageInput);
        chatPanel.setWidth("600px");
        chatPanel.setHeightFull();
        chatPanel.setPadding(false);
        chatPanel.setSpacing(false);
        chatPanel.expand(messageList);

        // Content panel (right side — add components here)
        var contentPanel = new VerticalLayout(new Span("Content Area"));
        contentPanel.setHeightFull();
        contentPanel.setPadding(true);

        // Orchestrator
        String systemPrompt = null;
        AIOrchestrator.builder(provider, systemPrompt)
                .withMessageList(messageList)
                .withInput(messageInput)
                .withFileReceiver(uploadManager)
                .build();

        // Main layout
        var mainLayout = new HorizontalLayout(chatPanel, contentPanel);
        mainLayout.setSizeFull();
        mainLayout.expand(contentPanel);
        setContent(mainLayout);
    }
}
