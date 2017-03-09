package com.inferno.gui;

import com.inferno.TetrisGame;
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
 * Created by Fernando on 3/8/2017.
 */
public class LossGUI extends GUI {
    private TextElement lossText;
    private Panel lossPanel;

    private Button retryButton;
    private Button exitButton;

    private Stopwatch lossTimer;
    private Stopwatch lossButtonTimer;

    private String lastCircle;

    public LossGUI(Game game, String lastCircle) {
        super(game);
        this.lastCircle = lastCircle;
    }

    public void load() {
        lossPanel = new Panel(new Vector2f(0, 0), new Vector2f(800, 600), new Color(0, 0, 0, 0), new Texture("textures/solid.png"));
        lossPanel.setDepth(-50);

        addElement(lossPanel);

        addElement(lossText = TextElement.centerWithin("You lost.", "fonts/Roboto Mono/RobotoMono-Bold.ttf", 64, new Color(0, 0, 0, 0), lossPanel));

        lossTimer = new Stopwatch(4.0f, () -> {}, () -> {
            exitButton = new Button("Exit",
                    "fonts/Roboto Mono/RobotoMono-Regular.ttf",
                    24,
                    new Color(0, 0, 0),
                    new Color(0.5f, 0, 0, 0),
                    new Texture("textures/solid.png"),
                    new Texture("textures/solid.png"),
                    new Transform2D(new Vector2f(300, 145), new Vector2f(180, 50)),
                    -51);

            retryButton = new Button("Retry",
                    "fonts/Roboto Mono/RobotoMono-Regular.ttf",
                    24,
                    new Color(0, 0, 0),
                    new Color(0, 0.5f, 0, 0),
                    new Texture("textures/solid.png"),
                    new Texture("textures/solid.png"),
                    new Transform2D(new Vector2f(500, 145), new Vector2f(180, 50)),
                    -51);

            retryButton.setOnClick(() -> {
                ((TetrisGame) getGame()).signalRestart(lastCircle);
            });

            exitButton.setOnClick(() -> getGame().stop());

            addElement(retryButton);
            addElement(exitButton);

            lossButtonTimer = new Stopwatch(2.0f, () -> {}, () -> {});
            lossButtonTimer.setTickAction((time, timeLimit) -> {
                float alpha = time / timeLimit;

                Color exitButtonColor = new Color(0.5f, 0, 0, alpha);
                Color retryButtonColor = new Color(0, 0.5f, 0, alpha);

                retryButton.getRenderData().setTint(retryButtonColor);
                exitButton.getRenderData().setTint(exitButtonColor);
            });
            lossButtonTimer.start();
        });

        lossTimer.setTickAction((time, timeLimit) -> {
            float alpha = time / timeLimit;

            Color panelColor = new Color(0, 0, 0, alpha * 0.8f);
            Color textColor = new Color(0.5f, 0, 0, alpha);

            lossText.getRenderData().setTint(textColor);
            lossPanel.getRenderData().setTint(panelColor);
        });

        lossTimer.start();
    }

    public void unload() {}

    @Override
    public void fixedUpdate(float dt) {
        if (lossTimer != null) {
            lossTimer.tick(dt);
        }

        if (lossButtonTimer != null) {
            lossButtonTimer.tick(dt);
        }
    }
}
