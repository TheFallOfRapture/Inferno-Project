package com.inferno.gui;

import com.morph.engine.core.Game;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Texture;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.Panel;
import com.morph.engine.newgui.TextElement;

/**
 * Created by Fernando on 3/6/2017.
 */
public class SecondCircleGUI extends TetrisGUI {
    public SecondCircleGUI(Game game, int width, int height, float worldSize) {
        super(game, width, height, worldSize);
    }

    public void load() {
        super.load();

        addElement(new Panel(new Vector2f(150, 150), new Vector2f(500, 300), new Color(0, 0, 0), new Texture("textures/solid.png")));
        addElement(new TextElement("LOL IM A GREEDY ASSHOLE", "fonts/Ubuntu/Ubuntu-Regular.ttf", 16, new Color(1, 1, 1), new Vector2f(200, 200), -20));
    }
}
