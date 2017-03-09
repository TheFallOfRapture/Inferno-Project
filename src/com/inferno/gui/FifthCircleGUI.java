package com.inferno.gui;

import com.morph.engine.core.Game;
import com.morph.engine.graphics.Color;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.TextElement;

/**
 * Created by Fernando on 3/7/2017.
 */
public class FifthCircleGUI extends TetrisGUI {
    private TextElement piecesLeftText;
    private int initPieces;

    public FifthCircleGUI(Game game, int width, int height, float worldSize, int goal, int initPieces) {
        super(game, width, height, worldSize, goal);
        this.initPieces = initPieces;
    }

    public void load() {
        super.load();

        addElement(new TextElement("Circle 5", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(0.5f, 0.05f, 0.05f), new Vector2f(570, 60), -20));

        addElement(new TextElement("Pieces", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 64, new Color(1, 1, 1), new Vector2f(570, 280), -20));
        piecesLeftText = new TextElement(Integer.toString(initPieces), "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, new Color(0, 1, 0), new Vector2f(570, 240), -20);

        addElement(piecesLeftText);
    }

    public void updatePiecesLeft(int pieces, int pieceLimit) {
        removeElement(piecesLeftText);
        piecesLeftText = new TextElement(Integer.toString(pieces), "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, new Color(0, 1, 0), new Vector2f(570, 240), -20);

        Color green = new Color(0, 1, 0);
        Color red = new Color(1, 0, 0);

        float alpha = ((float) pieces) / ((float) pieceLimit);

        Color currentColor = green.scale(alpha).add(red.scale(1.0f - alpha));

        piecesLeftText.getRenderData().setTint(currentColor);
        addElement(piecesLeftText);
    }
}
