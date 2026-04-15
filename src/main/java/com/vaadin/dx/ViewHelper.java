package com.vaadin.dx;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.internal.JacksonUtils;

/**
 * Utility methods for common view operations.
 */
public class ViewHelper {

    private ViewHelper() {
    }

    /**
     * Extracts a string property from a JSON string.
     *
     * @param json
     *            the JSON string
     * @param propertyName
     *            the property name to extract
     * @return the property value as a string
     */
    public static String getStringProperty(String json,
            String propertyName) {
        return JacksonUtils.readTree(json).get(propertyName).asString();
    }

    /**
     * Sets the background color of a component from the UI thread.
     *
     * @param component
     *            the component to style
     * @param color
     *            a CSS color value (e.g., "blue", "#ff0000")
     */
    public static void setBackgroundColor(Component component,
            String color) {
        component.getUI().ifPresent(ui -> ui.access(() -> component
                .getElement().getStyle().set("background-color", color)));
    }

    /**
     * Builds a JSON schema string for a tool that accepts a single required
     * parameter.
     *
     * @param name
     *            the parameter name
     * @param type
     *            the JSON schema type (e.g., "string", "number")
     * @param description
     *            a human-readable description of the parameter
     * @return the JSON schema as a string
     */
    public static String buildParameterSchema(String name, String type,
                                               String description) {
        var schema = JacksonUtils.createObjectNode();
        schema.put("type", "object");

        var property = JacksonUtils.createObjectNode();
        property.put("type", type);
        property.put("description", description);

        var properties = JacksonUtils.createObjectNode();
        properties.set(name, property);
        schema.set("properties", properties);

        var required = JacksonUtils.createArrayNode();
        required.add(name);
        schema.set("required", required);

        return schema.toString();
    }
}
