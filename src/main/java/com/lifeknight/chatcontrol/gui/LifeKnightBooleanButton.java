package com.lifeknight.chatcontrol.gui;

import com.lifeknight.chatcontrol.variables.LifeKnightBoolean;
import net.minecraft.client.Minecraft;

import static com.lifeknight.chatcontrol.utilities.Utils.get2ndPanelCenter;

public class LifeKnightBooleanButton extends LifeKnightButton {
    private final LifeKnightBoolean lifeKnightBoolean;

    public LifeKnightBooleanButton(int componentId, LifeKnightBoolean lifeKnightBoolean, LifeKnightButton connectedButton) {
        super(componentId, lifeKnightBoolean.getAsString());
        this.lifeKnightBoolean = lifeKnightBoolean;
        int j;
        if ((j = Minecraft.getMinecraft().fontRendererObj.getStringWidth(buttonText) + 30) > this.width) {
            this.width = j;
            this.xPosition = get2ndPanelCenter() - this.width / 2;

            if (connectedButton != null) {
                connectedButton.xPosition = this.xPosition + this.width + 10;
            }
        }
    }

    @Override
    public void work() {
        lifeKnightBoolean.toggle();
        displayString = lifeKnightBoolean.getAsString();
    }
}
