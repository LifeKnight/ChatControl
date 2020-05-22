package com.lifeknight.chatcontrol.variables;

import com.lifeknight.chatcontrol.utilities.Chat;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.lifeknight.chatcontrol.mod.ChatControlMod.config;
import static com.lifeknight.chatcontrol.mod.ChatControlMod.variables;

public class LifeKnightStringList extends LifeKnightVariable {
    private ArrayList<String> value;

    public LifeKnightStringList(String name, String group, ArrayList<String> value) {
        super.name = name;
        super.group = group;
        this.value = value;
        variables.add(this);
    }

    public LifeKnightStringList(String name, String group) {
        this(name, group, new ArrayList<>());
        variables.add(this);
    }

    public ArrayList<String> getValue() {
        return value;
    }

    public void setValue(ArrayList<String> newValue) {
        value = newValue;
        config.updateConfigFromVariables();
        onSetValue();
    }

    public void addElement(String element) throws IOException {
        if (!value.contains(element)) {
            value.add(element);
            config.updateConfigFromVariables();
            onAddElement();
        } else {
            throw new IOException(name + " already contains " + element + "!");
        }
    }

    public void removeElement(String element) throws IOException {
        if (value.contains(element)) {
            value.remove(element);
            config.updateConfigFromVariables();
            onRemoveElement();
        } else {
            throw new IOException(name + " does not contain " + element + "!");
        }
    }

    public void clear() {
        value.clear();
        config.updateConfigFromVariables();
        onClear();
    }

    public void setValueFromCSV(String CSV) {
        if (CSV.contains(",")) {
            try {
                value = new ArrayList<>(Arrays.asList(CSV.split(",")));
            } catch (Exception e) {
                e.printStackTrace();
                Chat.queueChatMessageForConnection(EnumChatFormatting.RED + "An error occurred while extracting the value of \"" + name + "\" from the config; the value will be interpreted as " + value + ".");
            }
        } else {
            value = new ArrayList<>();
        }
    }

    public String toCSV() {
        StringBuilder result = new StringBuilder();

        for (String element: value) {
            result.append(element).append(",");
        }

        return result.toString();
    }

    public void onAddElement() {}

    public void onRemoveElement() {}

    public void onClear() {}

    public void onSetValue() {}
}
