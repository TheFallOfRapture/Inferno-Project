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

    private TextElement scoreText;
    private TextElement scoreIncrementText;

//    private TextElement lossText;
//    private Panel lossPanel;
//
//    private Button retryButton;
//    private Button exitButton;

    private Stopwatch pointTimer;
//    private Stopwatch lossTimer;
//    private Stopwatch lossButtonTimer;

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

        addElement(new TextElement("TETRIS", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(1, 1, 1), new Vector2f(90, 540), -20));
        addElement(new TextElement("from HELL", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(1, 0, 0), new Vector2f(75, 505), -20));

        addElement(new TextElement("Fernando Gonzalez", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(1, 1, 1), new Vector2f(32, 60), -20));

        addElement(new TextElement("Score", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 64, new Color(1, 1, 1), new Vector2f(570, 520), -20));
        scoreText = new TextElement("0", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, new Color(0, 1, 0), new Vector2f(570, 480), -20);

        addElement(scoreText);
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

    public void updateScore(int points) {
        this.score += points;
        removeElement(scoreText);
        addElement(scoreText = new TextElement(Integer.toString(score), "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, new Color(0, 1, 0), new Vector2f(570, 480), -20));

        Color scoreColor = new Color(0, points / 230f, 0);

        addElement(scoreIncrementText = new TextElement("+" + Integer.toString(points), "fonts/Roboto Mono/RobotoMono-Regular.ttf", 28, new Color(0, 0.4f, 0), new Vector2f(575, 450), -20));

        pointTimer = new Stopwatch(1.25f, () -> {}, () -> removeElement(scoreIncrementText));
        pointTimer.setTickAction((time, timeLimit) -> {
            Color start = scoreColor;
            Color finish = new Color(0.05f, 0.05f, 0.05f);
            float alpha = BlendingUtils.easeInQuartic(time / timeLimit);
            System.out.println(alpha);
//            float alpha = time / 2.5f;

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

//        if (lossTimer != null) {
//            lossTimer.tick(dt);
//        }
//
//        if (lossButtonTimer != null) {
//            lossButtonTimer.tick(dt);
//        }
    }

//    public void notifyLoss(String currentCircle) {
//        lossPanel = new Panel(new Vector2f(0, 0), new Vector2f(800, 600), new Color(0, 0, 0, 0), new Texture("textures/solid.png"));
//        lossPanel.setDepth(-50);
//
//        addElement(lossPanel);
//
//        addElement(lossText = TextElement.centerWithin("You lost.", "fonts/Roboto Mono/RobotoMono-Bold.ttf", 64, new Color(0, 0, 0, 0), lossPanel));
//
//        lossTimer = new Stopwatch(4.0f, () -> {}, () -> {
//            exitButton = new Button("Exit",
//                    "fonts/Roboto Mono/RobotoMono-Regular.ttf",
//                    24,
//                    new Color(0, 0, 0),
//                    new Color(0.5f, 0, 0, 0),
//                    new Texture("textures/solid.png"),
//                    new Texture("textures/solid.png"),
//                    new Transform2D(new Vector2f(300, 145), new Vector2f(180, 50)),
//                    -51);
//
//            retryButton = new Button("Retry",
//                    "fonts/Roboto Mono/RobotoMono-Regular.ttf",
//                    24,
//                    new Color(0, 0, 0),
//                    new Color(0, 0.5f, 0, 0),
//                    new Texture("textures/solid.png"),
//                    new Texture("textures/solid.png"),
//                    new Transform2D(new Vector2f(500, 145), new Vector2f(180, 50)),
//                    -51);
//
//            retryButton.setOnClick(() -> {
//                ((TetrisGame) getGame()).restartWithState(currentCircle);
//            });
//
//            exitButton.setOnClick(() -> getGame().stop());
//
//            addElement(retryButton);
//            addElement(exitButton);
//
//            lossButtonTimer = new Stopwatch(2.0f, () -> {}, () -> {});
//            lossButtonTimer.setTickAction((time, timeLimit) -> {
//                float alpha = time / timeLimit;
//
//                Color exitButtonColor = new Color(0.5f, 0, 0, alpha);
//                Color retryButtonColor = new Color(0, 0.5f, 0, alpha);
//
//                retryButton.getRenderData().setTint(retryButtonColor);
//                exitButton.getRenderData().setTint(exitButtonColor);
//            });
//            lossButtonTimer.start();
//        });
//
//        lossTimer.setTickAction((time, timeLimit) -> {
//            float alpha = time / timeLimit;
//
//            Color panelColor = new Color(0, 0, 0, alpha * 0.8f);
//            Color textColor = new Color(0.5f, 0, 0, alpha);
//
//            lossText.getRenderData().setTint(textColor);
//            lossPanel.getRenderData().setTint(panelColor);
//        });
//
//        lossTimer.start();
//    }
//
//    public void removeLossElements() {
//        removeElement(lossPanel);
//        removeElement(lossText);
//        removeElement(retryButton);
//        removeElement(exitButton);
//    }
}
