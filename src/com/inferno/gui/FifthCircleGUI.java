package com.inferno.gui;

import com.morph.engine.core.Game;
import com.morph.engine.graphics.Color;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.TextElement;

/**
 * Created by Fernando on 3/7/2017.
 */
public class FifthCircleGUI extends TetrisGUI {
    public FifthCircleGUI(Game game, int width, int height, float worldSize) {
        super(game, width, height, worldSize);
    }

    public void load() {
        super.load();

        addElement(new TextElement("Circle 5", "fonts/Ubuntu/Ubuntu-Regular.ttf", 24, new Color(0.5f, 0.05f, 0.05f), new Vector2f(570, 60), -20));
    }
}
