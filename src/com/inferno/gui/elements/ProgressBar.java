package com.inferno.gui.elements;

import com.morph.engine.graphics.components.RenderData;
import com.morph.engine.newgui.Element;
import com.morph.engine.physics.components.Transform2D;

/**
 * Created by Fernando on 3/5/2017.
 */
public class ProgressBar extends Element {
    private float amount;

    /*
    Progress Bar Design:

    Textures:
    - Bar Frame
    - Bar
     */

    public ProgressBar(Transform2D transform, RenderData data, int depth) {
        super(transform, data, depth);
        amount = 0;
    }

    public ProgressBar(Transform2D transform, RenderData data) {
        super(transform, data);
        amount = 0;
    }

    public ProgressBar(Transform2D transform) {
        super(transform);
        amount = 0;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }
}
