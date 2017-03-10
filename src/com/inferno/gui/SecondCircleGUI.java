package com.inferno.gui;

import com.inferno.TetrisGame;
import com.morph.engine.core.Game;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Texture;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.Button;
import com.morph.engine.newgui.Panel;
import com.morph.engine.newgui.TextElement;
import com.morph.engine.physics.components.Transform2D;

/**
 * Created by Fernando on 3/6/2017.
 */
public class SecondCircleGUI extends TetrisGUI {
    public SecondCircleGUI(Game game, int width, int height, float worldSize) {
        super(game, width, height, worldSize, 0);
    }

    public void load() {
        super.load();

        addElement(new TextElement("Circle 2", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(0.5f, 0.05f, 0.05f), new Vector2f(570, 60), -20));

        Panel panel = new Panel(new Vector2f(100, 100), new Vector2f(600, 400), new Color(0, 0, 0), new Texture("textures/solid.png"));
        panel.setDepth(-35);

        addElement(panel);

        TextElement headerText = TextElement.centerXWithin("Thanks for playing!", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 16, new Color(0, 1, 0), panel);
        TextElement titleText = TextElement.centerXWithin("Tetris: From Hell - The Inner Depths", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 16, new Color(1, 1, 1), panel);

        Vector2f headerPos = new Vector2f(headerText.getTransform().getPosition().getX(), 470);
        Vector2f titlePos = new Vector2f(titleText.getTransform().getPosition().getX(), 450);

        TextElement ht = new TextElement("Thanks for playing!", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 16, new Color(0, 0.5f, 0), headerPos, -40);
        TextElement tt = new TextElement("Tetris: From Hell - The Inner Depths", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 16, new Color(1, 1, 1), titlePos, -40);

        addElement(ht);
        addElement(tt);

        Panel logo = new Panel(new Vector2f(275, 185), new Vector2f(250, 250), new Color(1, 1, 1), new Texture("textures/logo.png"));
        logo.setDepth(-37);
        addElement(logo);

        Button exitButton = new Button("Exit",
                "fonts/Roboto Mono/RobotoMono-Regular.ttf",
                18,
                new Color(0, 0, 0),
                new Color(0, 0.5f, 0),
                new Texture("textures/solid.png"),
                new Texture("textures/solid.png"),
                new Transform2D(new Vector2f(300, 145), new Vector2f(180, 50)),
                -36);

        Button nextButton = new Button("Purchase DLC ($4.99)",
                "fonts/Roboto Mono/RobotoMono-Regular.ttf",
                18,
                new Color(0, 0, 0),
                new Color(0.5f, 0, 0),
                new Texture("textures/solid.png"),
                new Texture("textures/solid.png"),
                new Transform2D(new Vector2f(500, 145), new Vector2f(180, 50)),
                -36);

        exitButton.setOnClick(getGame()::stop);
        nextButton.setOnClick(() -> ((TetrisGame)getGame()).getStateMachine().changeState("Circle 3"));

        addElement(nextButton);
        addElement(exitButton);
    }
}
