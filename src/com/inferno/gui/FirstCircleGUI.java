package com.inferno.gui;

import com.morph.engine.core.Game;
import com.morph.engine.graphics.Color;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.TextElement;

/**
 * Created by Fernando on 3/6/2017.
 */
public class FirstCircleGUI extends TetrisGUI {
    private TextElement qtEventButton;
    private float timerMax;

    public FirstCircleGUI(Game game, int width, int height, float worldSize) {
        super(game, width, height, worldSize);
    }

    public void load() {
        super.load();

        addElement(new TextElement("Circle 1", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(0.5f, 0.05f, 0.05f), new Vector2f(570, 60), -20));
    }

    public void activateQuicktimeEvent(String button, float timer) {
        qtEventButton = new TextElement(button, "fonts/Roboto Mono/RobotoMono-Regular.ttf", 48, new Color(1, 0, 0), new Vector2f(570, 200), -20);

        this.timerMax = timer;

        addElement(qtEventButton);
    }

    public void deactivateQuicktimeEvent() {
        removeElement(qtEventButton);
    }

    public void updateTimer(float timer) {
        Color red = new Color(1, 0, 0);
        Color green = new Color(0, 1, 0);
        float alpha = timer / timerMax;

        Color currentColor = green.scale(alpha).add(red.scale(1 - alpha));

        qtEventButton.getRenderData().setTint(currentColor);
    }
}
