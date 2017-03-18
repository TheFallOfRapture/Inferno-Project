package com.inferno.gui;

import com.morph.engine.core.Game;
import com.morph.engine.graphics.Color;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.TextElement;

/**
 * Created by Fernando on 3/18/2017.
 */
public class InfiniteTetrisGUI extends TetrisGUI {
    public InfiniteTetrisGUI(Game game, int width, int height, float worldSize) {
        super(game, width, height, worldSize, -1);
    }

    @Override
    public void load() {
        super.load();

        disableGoalTracking();
        addElement(new TextElement("Infinite Tetris", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(0, 0.75f, 0), new Vector2f(570, 60), -20));
    }
}
