package com.vaadin.dx;

import java.io.Serializable;

import com.vaadin.flow.server.VaadinSession;

/**
 * Simple state storage backed by the Vaadin session. Use this to persist
 * and retrieve data across page refreshes.
 *
 * <pre>
 * // Save
 * String key = StateStorage.persist(myState);
 *
 * // Retrieve
 * MyState restored = (MyState) StateStorage.retrieve(key);
 * </pre>
 */
public class StateStorage {

    private StateStorage() {
    }

    /**
     * Persists data to the session under a specific key, replacing any
     * previously stored value for that key.
     *
     * @param key
     *            the key to store under
     * @param data
     *            the data to persist, must be serializable
     */
    public static void persist(String key, Serializable data) {
        VaadinSession.getCurrent().setAttribute(key, data);
    }

    /**
     * Retrieves previously persisted data.
     *
     * @param key
     *            the key returned by {@link #persist(Serializable)} or
     *            passed to {@link #persist(String, Serializable)}
     * @return the stored data, or {@code null} if not found
     */
    public static Object retrieve(String key) {
        return VaadinSession.getCurrent().getAttribute(key);
    }
}
