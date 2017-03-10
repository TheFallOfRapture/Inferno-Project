package com.inferno.gui;

import com.inferno.util.BlendingUtils;
import com.inferno.util.Stopwatch;
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

    private Stopwatch qtMessageTimer;
    private TextElement qtMessage;

    public FirstCircleGUI(Game game, int width, int height, float worldSize, int goal) {
        super(game, width, height, worldSize, goal);
    }

    public void load() {
        super.load();

        addElement(new TextElement("Circle 1", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(0.5f, 0.05f, 0.05f), new Vector2f(570, 60), -20));
    }

    public void activateQuicktimeEvent(String button, float timer) {
        qtEventButton = new TextElement("Press " + button + "!", "fonts/Roboto Mono/RobotoMono-Bold.ttf", 56, new Color(1, 0, 0), new Vector2f(570, 200), -20);

        this.timerMax = timer;

        addElement(qtEventButton);

        if (qtMessageTimer != null)
            qtMessageTimer.interrupt();
    }

    public void deactivateQuicktimeEvent() {
        removeElement(qtEventButton);
    }

    public void passQuicktimeEvent(String msg) {
        Color msgColor;

        switch (msg) {
            case "Great!":
                msgColor = new Color(0, 1, 1);
                break;
            case "Good!":
                msgColor = new Color(0, 0.75f, 0);
                break;
            case "Close!":
                msgColor = new Color(1, 1, 0);
                break;
            default:
                msgColor = new Color(1, 0, 0);
                break;
        }

        addElement(qtMessage = new TextElement(msg, "fonts/Roboto Mono/RobotoMono-Bold.ttf", 56, msgColor, new Vector2f(570, 200), -20));

        qtMessageTimer = new Stopwatch(1.5f, () -> removeElement(qtMessage), () -> removeElement(qtMessage));
        qtMessageTimer.setTickAction((time, timeLimit) -> {
            Color start = msgColor;
            Color finish = new Color(0.05f, 0.05f, 0.05f);
            float alpha = BlendingUtils.easeInQuartic(time / timeLimit);

            Color currentColor = start.scale(alpha).add(finish.scale(1 - alpha));

            qtMessage.getRenderData().setTint(currentColor);
        });
        qtMessageTimer.start();
    }

    public void failQuicktimeEvent() {
        addElement(qtMessage = new TextElement("Ouch!", "fonts/Roboto Mono/RobotoMono-Bold.ttf", 56, new Color(1, 0, 0), new Vector2f(570, 200), -20));
        qtMessageTimer = new Stopwatch(1.5f, () -> removeElement(qtMessage), () -> removeElement(qtMessage));
        qtMessageTimer.setTickAction((time, timeLimit) -> {
            Color start = new Color(1, 0, 0);
            Color finish = new Color(0.05f, 0.05f, 0.05f);
            float alpha = BlendingUtils.easeInQuartic(time / timeLimit);

            Color currentColor = start.scale(alpha).add(finish.scale(1 - alpha));

            qtMessage.getRenderData().setTint(currentColor);
        });
        qtMessageTimer.start();
    }

    public void updateTimer(float timer) {
        Color red = new Color(1, 0, 0);
        Color green = new Color(0, 1, 0);
        float alpha = timer / timerMax;

        Color currentColor = green.scale(alpha).add(red.scale(1 - alpha));

        qtEventButton.getRenderData().setTint(currentColor);
    }

    @Override
    public void fixedUpdate(float dt) {
        super.fixedUpdate(dt);

        if (qtMessageTimer != null) {
            qtMessageTimer.tick(dt);
        }
    }

    public void onLoss() {
        qtMessageTimer.endTimer();
    }
}
