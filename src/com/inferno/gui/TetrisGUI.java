package com.inferno.gui;

import com.inferno.TetrisGame;
import com.inferno.util.BlendingUtils;
import com.inferno.util.Stopwatch;
import com.morph.engine.core.Game;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Texture;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.Button;
import com.morph.engine.newgui.GUI;
import com.morph.engine.newgui.Panel;
import com.morph.engine.newgui.TextElement;
import com.morph.engine.physics.components.Transform2D;

/**
 * Created by Fernando on 3/6/2017.
 */
public class TetrisGUI extends GUI {
    private float worldSize;
    protected int width;
    protected int height;
    private int score;
    private int goal;

    private TextElement scoreText;
    private TextElement scoreIncrementText;

    private Stopwatch pointTimer;

    public TetrisGUI(Game game, int width, int height, float worldSize, int goal) {
        super(game);
        this.width = width;
        this.height = height;
        this.worldSize = worldSize;
        this.score = 0;
        this.goal = goal;
    }

    @Override
    public void load() {
        float panelWidth = 12.5f * worldSize;

        addElement(new Panel(new Vector2f(0, 0), new Vector2f(panelWidth, height), new Color(0.05f, 0.05f, 0.05f), new Texture("textures/solid.png")));
        addElement(new Panel(new Vector2f(width - panelWidth, 0), new Vector2f(panelWidth, height), new Color(0.05f, 0.05f, 0.05f), new Texture("textures/solid.png")));

        addElement(new TextElement("TETRIS", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(1, 1, 1), new Vector2f(160, 540), -20));
        addElement(new TextElement("from HELL", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(0.5f, 0.05f, 0.05f), new Vector2f(130, 505), -20));

        addElement(new TextElement("Fernando Gonzalez", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(1, 1, 1), new Vector2f(39, 60), -20));

        addElement(new TextElement("Score", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 64, new Color(1, 1, 1), new Vector2f(570, 520), -20));
        scoreText = new TextElement("0", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, new Color(0, 1, 0), new Vector2f(570, 480), -20);

        addElement(scoreText);

        addElement(new TextElement("Goal", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 64, new Color(1, 1, 1), new Vector2f(570, 400), -20));
        addElement(new TextElement(Integer.toString(goal), "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, new Color(0, 1, 0), new Vector2f(570, 360), -20));
    }

    @Override
    public void unload() {

    }

    public void resetScore() {
        this.score = 0;
        removeElement(scoreText);
        scoreText = new TextElement(Integer.toString(score), "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, new Color(0, 1, 0), new Vector2f(570, 480), -20);
        addElement(scoreText);
    }

    public void updateScore(int points, int lines) {
        this.score += points;
        removeElement(scoreText);
        addElement(scoreText = new TextElement(Integer.toString(score), "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, new Color(0, 1, 0), new Vector2f(570, 480), -20));

        Color scoreColor = new Color(0, ((points / 230f) * 0.5f) + 0.5f, 0);

        String msg = "";
        switch (lines) {
            case 1:
                msg = "Single";
                break;
            case 2:
                msg = "Double";
                break;
            case 3:
                msg = "Triple";
                break;
            case 4:
                msg = "Tetris";
                break;
        }

        addElement(scoreIncrementText = new TextElement("+" + Integer.toString(points) + " [" + msg + "]", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 28, new Color(0, 0.4f, 0), new Vector2f(575, 450), -20));

        pointTimer = new Stopwatch(1.25f, () -> {}, () -> removeElement(scoreIncrementText));
        pointTimer.setTickAction((time, timeLimit) -> {
            Color start = scoreColor;
            Color finish = new Color(0.05f, 0.05f, 0.05f);
            float alpha = BlendingUtils.easeInQuartic(time / timeLimit);
            System.out.println(alpha);

            Color currentColor = start.scale(alpha).add(finish.scale(1 - alpha));

            scoreIncrementText.getRenderData().setTint(currentColor);
        });
        pointTimer.start();
    }

    @Override
    public void fixedUpdate(float dt) {
        if (pointTimer != null) {
            pointTimer.tick(dt);
        }
    }
}
