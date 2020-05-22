package com.lifeknight.chatcontrol.variables;

import static com.lifeknight.chatcontrol.mod.ChatControlMod.config;
import static com.lifeknight.chatcontrol.mod.ChatControlMod.variables;

public class LifeKnightDouble extends LifeKnightVariable {
    private double value;
    private double minimumValue = Double.MIN_VALUE;
    private double maximumValue = Double.MAX_VALUE;

    public LifeKnightDouble(String name, String group, double value) {
        super.name = name;
        super.group = group;
        this.value = value;
        variables.add(this);
    }

    public LifeKnightDouble(String name, String group, double value, double minimumValue, double maximumValue) {
        this(name, group, value);
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
    }

    public double getMinimumValue() {
        return minimumValue;
    }

    public double getMaximumValue() {
        return maximumValue;
    }

    public void setMinimumValue(double minimumValue) {
        this.minimumValue = minimumValue;
        onSetMinimumValue();
    }

    public void setMaximumValue(double maximumValue) {
        this.maximumValue = maximumValue;
        onSetMaximumValue();
    }

    public Double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        if (config != null) {
            config.updateConfigFromVariables();
            onSetValue();
        }
    }

    public void onSetValue() {}

    public void onSetMinimumValue() {}

    public void onSetMaximumValue() {}
}
