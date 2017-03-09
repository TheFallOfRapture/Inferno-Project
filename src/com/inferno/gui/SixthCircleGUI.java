package com.inferno.gui;

import com.inferno.util.Stopwatch;
import com.morph.engine.core.Game;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Texture;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.Panel;
import com.morph.engine.newgui.TextElement;

/**
 * Created by Fernando on 3/9/2017.
 */
public class SixthCircleGUI extends TetrisGUI {
    private Stopwatch bsodTimer;
    private Stopwatch crashTimer;

    public SixthCircleGUI(Game game, int width, int height, float worldSize) {
        super(game, width, height, worldSize, 0);
    }

    @Override
    public void load() {
        super.load();

        addElement(new TextElement("Circle 6", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, new Color(0.5f, 0.05f, 0.05f), new Vector2f(570, 60), -20));

        Panel bsod = new Panel(new Vector2f(0, 0), new Vector2f(800, 600), new Texture("textures/bsod.png"));
        bsod.setDepth(-100);

        bsodTimer = new Stopwatch(3.0f, () -> {}, () -> addElement(bsod));
        crashTimer = new Stopwatch(6.0f, () -> {}, this.getGame()::stop);
        bsodTimer.start();
        crashTimer.start();
    }

    @Override
    public void fixedUpdate(float dt) {
        super.fixedUpdate(dt);

        if (bsodTimer != null) {
            bsodTimer.tick(dt);
        }

        if (crashTimer != null) {
            crashTimer.tick(dt);
        }
    }
}
