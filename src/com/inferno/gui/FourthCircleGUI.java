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
public class FourthCircleGUI extends TetrisGUI {
    public FourthCircleGUI(Game game, int width, int height, float worldSize) {
        super(game, width, height, worldSize);
    }

    public void load() {
        super.load();
        addElement(new Panel(new Vector2f(0, 0), new Vector2f(width, height), new Color(1, 1, 1, 0.2f), new Texture("textures/mass effect 3.jpg")));

        addElement(new TextElement("Circle 4", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(0.5f, 0.05f, 0.05f), new Vector2f(570, 60), -20));

        Panel panel = new Panel(new Vector2f(50, 100), new Vector2f(700, 400), new Color(0, 0, 0), new Texture("textures/solid.png"));
        panel.setDepth(-35);

        addElement(panel);

        TextElement headerText = TextElement.centerXWithin("Tetris: From Hell - The Inner Depths", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 16, new Color(0, 1, 0), panel);
        TextElement titleText = TextElement.centerXWithin("Circle 4 - Meaningless Choice", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 16, new Color(1, 1, 1), panel);

        Vector2f headerPos = new Vector2f(headerText.getTransform().getPosition().getX(), 470);
        Vector2f titlePos = new Vector2f(titleText.getTransform().getPosition().getX(), 450);

        TextElement ht = new TextElement("Tetris: From Hell - The Inner Depths", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 16, new Color(1, 1, 1), headerPos, -40);
        TextElement tt = new TextElement("Circle 4 - Meaningless Choice", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 16, new Color(0.5f, 0, 0), titlePos, -40);

        addElement(ht);
        addElement(tt);

        TextElement temp1 = TextElement.centerXWithin("Choose your favorite color.", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(0, 1, 0), panel);
        TextElement temp2 = TextElement.centerXWithin("Your choice will have irreversible consequences on the rest of the game.", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 20, new Color(1, 1, 1), panel);

        Vector2f temp1Pos = new Vector2f(temp1.getTransform().getPosition().getX(), 320);
        Vector2f temp2Pos = new Vector2f(temp2.getTransform().getPosition().getX(), 280);

        TextElement t1 = new TextElement("Choose your favorite color.", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(1, 1, 1), temp1Pos, -40);
        TextElement t2 = new TextElement("Your choice will have irreversible consequences on the rest of the game.", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 20, new Color(0.5f, 0, 0), temp2Pos, -40);

        addElement(t1);
        addElement(t2);

        Button redButton = new Button("Red",
                "fonts/Roboto Mono/RobotoMono-Regular.ttf",
                18,
                new Color(0, 0, 0),
                new Color(1, 0, 0),
                new Texture("textures/solid.png"),
                new Texture("textures/solid.png"),
                new Transform2D(new Vector2f(300, 145), new Vector2f(80, 50)),
                -36);

        Button greenButton = new Button("Green",
                "fonts/Roboto Mono/RobotoMono-Regular.ttf",
                18,
                new Color(0, 0, 0),
                new Color(0, 1, 0),
                new Texture("textures/solid.png"),
                new Texture("textures/solid.png"),
                new Transform2D(new Vector2f(400, 145), new Vector2f(80, 50)),
                -36);

        Button blueButton = new Button("Blue",
                "fonts/Roboto Mono/RobotoMono-Regular.ttf",
                18,
                new Color(0, 0, 0),
                new Color(0, 0, 1),
                new Texture("textures/solid.png"),
                new Texture("textures/solid.png"),
                new Transform2D(new Vector2f(500, 145), new Vector2f(80, 50)),
                -36);

        redButton.setOnClick(() -> ((TetrisGame)getGame()).getStateMachine().changeState("Circle 4: Red"));
        greenButton.setOnClick(() -> ((TetrisGame)getGame()).getStateMachine().changeState("Circle 4: Green"));
        blueButton.setOnClick(() -> ((TetrisGame)getGame()).getStateMachine().changeState("Circle 4: Blue"));

        addElement(redButton);
        addElement(greenButton);
        addElement(blueButton);
    }
}
