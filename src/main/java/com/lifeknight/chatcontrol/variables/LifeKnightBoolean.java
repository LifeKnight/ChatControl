package com.lifeknight.chatcontrol.variables;


import static com.lifeknight.chatcontrol.mod.ChatControlMod.config;
import static com.lifeknight.chatcontrol.mod.ChatControlMod.variables;
import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.RED;

public class LifeKnightBoolean extends LifeKnightVariable {
    private boolean value;
    private LifeKnightStringList lifeKnightStringList;

    public LifeKnightBoolean(String name, String group, boolean value) {
        super.name = name;
        super.group = group;
        this.value = value;
        variables.add(this);
    }

    public LifeKnightBoolean(String name, String group, boolean value, LifeKnightStringList lifeKnightStringList) {
        this(name, group, value);
        this.lifeKnightStringList = lifeKnightStringList;
    }

    public boolean hasStringList() {
        return lifeKnightStringList != null;
    }

    public LifeKnightStringList getLifeKnightStringList() {
        return lifeKnightStringList;
    }

    public Boolean getValue() {
        return value;
    }

    public void toggle() {
        value = !value;
        if (!config.active) {
            config.updateConfigFromVariables();
            onSetValue();
        }
    }

    public void setValue(boolean newValue) {
        value = newValue;
        //noinspection ConstantConditions
        if (config != null) {
            config.updateConfigFromVariables();
            onSetValue();
        }
    }

    public String getAsString() {
        if (value) {
            return name + ": " + GREEN + "ENABLED";
        } else {
            return name + ": " + RED + "DISABLED";
        }
    }

    public void onSetValue() {}
}
