package com.qpros.reporting;

import java.util.HashMap;

class StateHelper {
    private static HashMap<String, Object> CrossStepState = new HashMap<>();
    public static void setStepState(String key, String value)
    {
        CrossStepState.put(key, value);
    }

    public static Object getStepState(String key)
    {
        return CrossStepState.get(key);
    }

    public static void clearStepState()
    {
        CrossStepState.clear();
    }
}
