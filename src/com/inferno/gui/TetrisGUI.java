package com.inferno.gui;

import com.morph.engine.core.Game;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Texture;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.GUI;
import com.morph.engine.newgui.Panel;
import com.morph.engine.newgui.TextElement;

/**
 * Created by Fernando on 3/6/2017.
 */
public class TetrisGUI extends GUI {
    private float worldSize;
    protected int width;
    protected int height;
    private int score;

    private TextElement scoreText;

    public TetrisGUI(Game game, int width, int height, float worldSize) {
        super(game);
        this.width = width;
        this.height = height;
        this.worldSize = worldSize;
        this.score = 0;
    }

    @Override
    public void load() {
        float panelWidth = 12.5f * worldSize;

        addElement(new Panel(new Vector2f(0, 0), new Vector2f(panelWidth, height), new Color(0.05f, 0.05f, 0.05f), new Texture("textures/solid.png")));
        addElement(new Panel(new Vector2f(width - panelWidth, 0), new Vector2f(panelWidth, height), new Color(0.05f, 0.05f, 0.05f), new Texture("textures/solid.png")));

        addElement(new TextElement("TETRIS", "fonts/Ubuntu/Ubuntu-Regular.ttf", 24, new Color(1, 1, 1), new Vector2f(90, 540), -20));
        addElement(new TextElement("from HELL", "fonts/Ubuntu/Ubuntu-Regular.ttf", 24, new Color(1, 0, 0), new Vector2f(75, 505), -20));

        addElement(new TextElement("Fernando Gonzalez", "fonts/Ubuntu/Ubuntu-Regular.ttf", 24, new Color(1, 1, 1), new Vector2f(32, 60), -20));

        addElement(new TextElement("Score", "fonts/Ubuntu/Ubuntu-Regular.ttf", 64, new Color(1, 1, 1), new Vector2f(570, 520), -20));
        scoreText = new TextElement("0", "fonts/Ubuntu/Ubuntu-Regular.ttf", 32, new Color(0, 1, 0), new Vector2f(570, 480), -20);

        addElement(scoreText);
    }

    @Override
    public void unload() {

    }

    public void updateScore(int score) {
        this.score = score;
        removeElement(scoreText);
        scoreText = new TextElement(Integer.toString(score), "fonts/Ubuntu/Ubuntu-Regular.ttf", 32, new Color(0, 1, 0), new Vector2f(570, 480), -20);
        addElement(scoreText);
    }
}
