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
public class FourthCircleRedGUI extends TetrisGUI {
    public FourthCircleRedGUI(Game game, int width, int height, float worldSize) {
        super(game, width, height, worldSize);
    }

    public void load() {
        addElement(new Panel(new Vector2f(0, 0), new Vector2f(width, height), new Color(1, 0, 0), new Texture("textures/mass effect 3.jpg")));

        Panel panel = new Panel(new Vector2f(100, 100), new Vector2f(600, 400), new Color(0, 0, 0, 0.5f), new Texture("textures/solid.png"));
        panel.setDepth(-35);

        addElement(panel);

        TextElement a = TextElement.centerXWithin("Congratulations!", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, new Color(1, 1, 1), panel);
        TextElement b = TextElement.centerXWithin("You got the red ending!", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, new Color(1, 0, 0), panel);

        Vector2f aa = new Vector2f(a.getTransform().getPosition().getX(), 470);
        Vector2f bb = new Vector2f(b.getTransform().getPosition().getX(), 430);

        TextElement aaa = new TextElement("Congratulations!", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, new Color(1, 1, 1), aa, -40);
        TextElement bbb = new TextElement("You got the red ending!", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, new Color(1, 0, 0), bb, -40);

        addElement(aaa);
        addElement(bbb);

        Button nextButton = new Button("Circle 5",
                "fonts/Roboto Mono/RobotoMono-Regular.ttf",
                18,
                new Color(0, 0, 0),
                new Color(1, 1, 1),
                new Texture("textures/solid.png"),
                new Texture("textures/solid.png"),
                new Transform2D(new Vector2f(400, 145), new Vector2f(150, 50)),
                -36);

        nextButton.setOnClick(() -> ((TetrisGame)getGame()).getStateMachine().changeState("Circle 5"));

        Panel me3DLCImage = new Panel(new Vector2f(245, 214), new Vector2f(310, 193), new Texture("textures/mass effect dlc.jpg"));
        me3DLCImage.setDepth(-36);
        addElement(me3DLCImage);

        addElement(nextButton);
    }
}
