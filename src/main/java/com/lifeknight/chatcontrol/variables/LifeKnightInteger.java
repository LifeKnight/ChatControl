package com.lifeknight.chatcontrol.variables;

import static com.lifeknight.chatcontrol.mod.ChatControlMod.configuration;
import static com.lifeknight.chatcontrol.mod.ChatControlMod.variables;

public class LifeKnightInteger extends LifeKnightVariable {
    private int value;
    private int minimumValue = Integer.MIN_VALUE;
    private int maximumValue = Integer.MAX_VALUE;

    public LifeKnightInteger(String name, String group, int value) {
        super.name = name;
        super.group = group;
        this.value = value;
        variables.add(this);
    }

    public LifeKnightInteger(String name, String group, int value, int minimumValue, int maximumValue) {
        this(name, group, value);
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
    }

    public int getMinimumValue() {
        return minimumValue;
    }

    public int getMaximumValue() {
        return maximumValue;
    }

    public void setMinimumValue(int minimumValue) {
        this.minimumValue = minimumValue;
        onSetMinimumValue();
    }

    public void setMaximumValue(int maximumValue) {
        this.maximumValue = maximumValue;
        onSetMaximumValue();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(int newValue) {
        value = newValue;
        if (configuration != null) {
            configuration.updateConfigFromVariables();
            onSetValue();
        }
    }

    public void onSetValue() {}

    public void onSetMinimumValue() {}

    public void onSetMaximumValue() {}

}
