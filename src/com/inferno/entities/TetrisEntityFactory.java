package com.inferno.entities;

import com.inferno.entities.components.Ad;
import com.morph.engine.entities.Entity;
import com.morph.engine.entities.EntityFactory;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Texture;
import com.morph.engine.graphics.components.RenderData;
import com.morph.engine.math.Vector2f;
import com.morph.engine.physics.components.Transform2D;
import com.inferno.graphics.shaders.TetrisShader;

/**
 * Created by Fernando on 2/11/2017.
 */
public class TetrisEntityFactory {
    public static Entity getBlock(String name, Color c, float size) {
        Entity block = EntityFactory.getCustomTintRectangle(name, size, size, c, new TetrisShader());
        block.getComponent(RenderData.class).setTexture(new Texture("textures/tetrisBlockSV.png"), 0);
        return block;
    }

    public static Entity getAdBlock(float size) {
        Entity block = EntityFactory.getRectangle("ad", size, size, new Color(1, 1, 1));
        block.getComponent(RenderData.class).setTexture(new Texture("textures/ad.jpg"), 0);
        block.addComponent(new Ad());
        return block;
    }

    public static Entity getBlockAt(String name, Vector2f position, Color c, float size) {
        Entity block = getBlock(name, c, size);
        block.getComponent(Transform2D.class).setPosition(position);
        return block;
    }
}
