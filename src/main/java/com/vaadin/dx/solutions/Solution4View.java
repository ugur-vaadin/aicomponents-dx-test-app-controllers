package com.vaadin.dx.solutions;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import com.vaadin.dx.StateStorage;
import com.vaadin.flow.component.ai.chart.ChartAIController;
import com.vaadin.flow.component.ai.chart.ChartState;
import com.vaadin.flow.component.ai.grid.GridAIController;
import com.vaadin.flow.component.ai.grid.GridState;
import com.vaadin.flow.component.ai.orchestrator.AIOrchestrator;
import com.vaadin.flow.component.ai.provider.SpringAILLMProvider;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Solution 4: State persistence across page refresh using StateStorage.
 */
@Route("solution4")
public class Solution4View extends VerticalLayout {

    public Solution4View(DataSource dataSource) {
        var db = new Solution1View.H2DatabaseProvider(dataSource);

        // LLM provider
        var openAiApi = OpenAiApi.builder()
                .apiKey(System.getenv("OPENAI_API_KEY")).build();
        var chatModel = OpenAiChatModel.builder().openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4o-mini").build())
                .build();
        var provider = new SpringAILLMProvider(chatModel);

        // Grid + controller + state restore
        var grid = new Grid<Map<String, Object>>();
        grid.setHeight("350px");
        grid.setWidthFull();
        var gridController = new GridAIController(grid, db);

        var savedGridState = (GridState) StateStorage.retrieve("grid-state");
        if (savedGridState != null) {
            gridController.restoreState(savedGridState);
        }

        // Chart + controller + state restore
        var chart = new Chart();
        chart.setHeight("350px");
        chart.setWidthFull();
        var chartController = new ChartAIController(chart, db);

        var savedChartState = (ChartState) StateStorage
                .retrieve("chart-state");
        if (savedChartState != null) {
            chartController.restoreState(savedChartState);
        }

        // Save state on change
        var gridStatus = new Span("Grid: empty");
        gridStatus.getStyle().set("font-size", "0.85em")
                .set("color", "#666");
        gridController.addStateChangeListener(state -> {
            StateStorage.persist("grid-state", state);
            getUI().ifPresent(ui -> ui.access(
                    () -> gridStatus.setText("Grid: " + state.query())));
        });

        var chartStatus = new Span("Chart: empty");
        chartStatus.getStyle().set("font-size", "0.85em")
                .set("color", "#666");
        chartController.addStateChangeListener(state -> {
            StateStorage.persist("chart-state", state);
            getUI().ifPresent(ui -> ui.access(() -> chartStatus
                    .setText("Chart: " + state.queries().size()
                            + " queries")));
        });

        // Combined system prompt
        var systemPrompt = GridAIController.getSystemPrompt() + "\n\n"
                + ChartAIController.getSystemPrompt();

        // Chat UI
        var messageList = new MessageList();
        messageList.setWidthFull();
        messageList.setMaxHeight("200px");
        var messageInput = new MessageInput();
        messageInput.setWidthFull();

        // Orchestrator
        var orchestrator = AIOrchestrator.builder(provider, systemPrompt)
                .withMessageList(messageList).withInput(messageInput)
                .withController(gridController).build();

        // Layout
        var gridSection = new VerticalLayout(new H3("Grid"), gridStatus,
                grid);
        gridSection.setPadding(false);
        gridSection.setWidth("50%");

        var chartSection = new VerticalLayout(new H3("Chart"),
                chartStatus, chart);
        chartSection.setPadding(false);
        chartSection.setWidth("50%");

        var visualizations = new HorizontalLayout(gridSection,
                chartSection);
        visualizations.setWidthFull();

        add(visualizations, messageList, messageInput);
        setSizeFull();
        setPadding(true);
    }
}
