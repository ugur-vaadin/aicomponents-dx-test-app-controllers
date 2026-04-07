package com.vaadin.dx.solutions;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import com.vaadin.flow.component.ai.chart.ChartAIController;
import com.vaadin.flow.component.ai.grid.GridAIController;
import com.vaadin.flow.component.ai.orchestrator.AIOrchestrator;
import com.vaadin.flow.component.ai.provider.SpringAILLMProvider;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Solution 3: Grid + Chart with shared orchestrator.
 */
@Route("solution3")
public class Solution3View extends VerticalLayout {

    public Solution3View(DataSource dataSource) {
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
        grid.setHeight("350px");
        grid.setWidthFull();
        var gridController = new GridAIController(grid, db);

        // Chart + controller
        var chart = new Chart();
        chart.setHeight("350px");
        chart.setWidthFull();
        var chartController = new ChartAIController(chart, db);

        // Combined system prompt
        var systemPrompt = GridAIController.getSystemPrompt() + "\n\n"
                + ChartAIController.getSystemPrompt();

        // Chat UI
        var messageList = new MessageList();
        messageList.setWidthFull();
        messageList.setMaxHeight("250px");
        var messageInput = new MessageInput();
        messageInput.setWidthFull();

        // Orchestrator — note: only one controller supported
        var orchestrator = AIOrchestrator.builder(provider, systemPrompt)
                .withMessageList(messageList).withInput(messageInput)
                .withController(gridController).build();

        // Layout
        var gridSection = new VerticalLayout(new H3("Grid"), grid);
        gridSection.setPadding(false);
        gridSection.setWidth("50%");

        var chartSection = new VerticalLayout(new H3("Chart"), chart);
        chartSection.setPadding(false);
        chartSection.setWidth("50%");

        var visualizations = new HorizontalLayout(gridSection, chartSection);
        visualizations.setWidthFull();

        add(visualizations, messageList, messageInput);
        setSizeFull();
        setPadding(true);
    }
}
