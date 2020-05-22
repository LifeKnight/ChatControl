package com.lifeknight.chatcontrol.variables;

public class LifeKnightVariable {
    public String name;
    public String group;

    public String getName() {
        return name;
    }

    public String getLowerCaseName() {
        return name.toLowerCase();
    }

    public String getGroup() {
        return group;
    }

    public String getLowerCaseGroup() {
        return group.toLowerCase();
    }

    public Object getValue() {
        return new Object();
    }

}
