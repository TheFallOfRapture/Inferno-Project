package com.inferno.gui;

import com.morph.engine.core.Game;
import com.morph.engine.graphics.Color;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.TextElement;

/**
 * Created by Fernando on 3/6/2017.
 */
public class ThirdCircleGUI extends TetrisGUI {
    private TextElement adBlockCdText;
    private float adBlockCooldown;
    private boolean ready = false;

    public ThirdCircleGUI(Game game, int width, int height, float worldSize, float adBlockCooldown, int goal) {
        super(game, width, height, worldSize, goal);
        this.adBlockCooldown = adBlockCooldown;
    }

    public void load() {
        super.load();

        addElement(new TextElement("Circle 3", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(0.5f, 0.05f, 0.05f), new Vector2f(570, 60), -20));
        addElement(new TextElement("AdBlock (B)", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 36, new Color(1, 1, 1), new Vector2f(570, 200), -20));
        adBlockCdText = new TextElement("Ready", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 48, new Color(0, 1, 0), new Vector2f(570, 150), -20);
        addElement(adBlockCdText);
    }

    public void setOnCooldown() {
        removeElement(adBlockCdText);
        adBlockCdText = new TextElement("Waiting...", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 48, new Color(1, 0, 0), new Vector2f(570, 150), -20);
        addElement(adBlockCdText);
    }

    public void setOffCooldown() {
        removeElement(adBlockCdText);
        adBlockCdText = new TextElement("Ready", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 48, new Color(0, 1, 0), new Vector2f(570, 150), -20);
        addElement(adBlockCdText);
    }

    public void updateCooldown(float timer) {
        Color red = new Color(1, 0, 0);
        Color green = new Color(0, 1, 0);
        float alpha = timer / adBlockCooldown;

        Color currentColor = green.scale(alpha).add(red.scale(1 - alpha));

        adBlockCdText.getRenderData().setTint(currentColor);
    }
}
