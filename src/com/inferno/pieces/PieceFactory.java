package com.inferno.pieces;

import com.morph.engine.entities.Entity;
import com.morph.engine.entities.EntityGrid;
import com.morph.engine.graphics.Color;
import com.inferno.entities.TetrisEntityFactory;

import java.util.List;

/**
 * Created by Fernando on 2/10/2017.
 */
public class PieceFactory {
    private static int count;
    public enum PieceType {
        I, O, T, J, L, S, Z, RANDOM
    }

    public static Piece getPiece(PieceType type) {
        switch (type) {
            case I:
                return getI();
            case O:
                return getO();
            case T:
                return getT();
            case J:
                return getJ();
            case L:
                return getL();
            case S:
                return getS();
            case Z:
                return getZ();
            case RANDOM:
                int rand = (int) (Math.random() * 7);
                return getPiece(rand);
            default:
                return null;
        }
    }

    public static Piece getPiece(int index) {
        switch (index) {
            case 0:
                return getI();
            case 1:
                return getO();
            case 2:
                return getT();
            case 3:
                return getJ();
            case 4:
                return getL();
            case 5:
                return getS();
            case 6:
                return getZ();
            default:
                // ???
                return null;
        }
    }

    private static Piece getI() {
        Piece piece = new Piece(4, 1);
        Entity[] blocks = new Entity[4];
        for (int i = 0; i < 4; i++) {
            blocks[i] = TetrisEntityFactory.getBlock("I-" + count + i, new Color(0, 1, 1), 1);
        }

        piece.addEntity(blocks[0], 0, 0);
        piece.addEntity(blocks[1], 1, 0);
        piece.addEntity(blocks[2], 2, 0);
        piece.addEntity(blocks[3], 3, 0);

        count += 4;

        return piece;
    }

    private static Piece getO() {
        Piece piece = new Piece(2, 2);
        Entity[] blocks = new Entity[4];
        for (int i = 0; i < 4; i++) {
            blocks[i] = TetrisEntityFactory.getBlock("O-" + count + i, new Color(1, 1, 0), 1);
        }

        piece.addEntity(blocks[0], 0, 0);
        piece.addEntity(blocks[1], 1, 0);
        piece.addEntity(blocks[2], 0, 1);
        piece.addEntity(blocks[3], 1, 1);

        count += 4;

        return piece;
    }

    private static Piece getT() {
        Piece piece = new Piece(3, 2);
        Entity[] blocks = new Entity[4];
        for (int i = 0; i < 4; i++) {
            blocks[i] = TetrisEntityFactory.getBlock("T-" + count + i, new Color(1, 0, 1), 1);
        }

        piece.addEntity(blocks[0], 1, 0);
        piece.addEntity(blocks[1], 0, 1);
        piece.addEntity(blocks[2], 1, 1);
        piece.addEntity(blocks[3], 2, 1);

        count += 4;

        return piece;
    }

    private static Piece getJ() {
        Piece piece = new Piece(3, 2);
        Entity[] blocks = new Entity[4];
        for (int i = 0; i < 4; i++) {
            blocks[i] = TetrisEntityFactory.getBlock("J-" + count + i, new Color(0, 0, 1), 1);
        }

        piece.addEntity(blocks[0], 0, 0);
        piece.addEntity(blocks[1], 0, 1);
        piece.addEntity(blocks[2], 1, 1);
        piece.addEntity(blocks[3], 2, 1);

        count += 4;

        return piece;
    }

    private static Piece getL() {
        Piece piece = new Piece(3, 2);
        Entity[] blocks = new Entity[4];
        for (int i = 0; i < 4; i++) {
            blocks[i] = TetrisEntityFactory.getBlock("L-" + count + i, new Color(1, 0.5f, 0), 1);
        }

        piece.addEntity(blocks[0], 2, 0);
        piece.addEntity(blocks[1], 0, 1);
        piece.addEntity(blocks[2], 1, 1);
        piece.addEntity(blocks[3], 2, 1);

        count += 4;

        return piece;
    }

    private static Piece getS() {
        Piece piece = new Piece(3, 2);
        Entity[] blocks = new Entity[4];
        for (int i = 0; i < 4; i++) {
            blocks[i] = TetrisEntityFactory.getBlock("S-" + count + i, new Color(0, 1, 0), 1);
        }

        piece.addEntity(blocks[0], 1, 0);
        piece.addEntity(blocks[1], 2, 0);
        piece.addEntity(blocks[2], 0, 1);
        piece.addEntity(blocks[3], 1, 1);

        count += 4;

        return piece;
    }

    private static Piece getZ() {
        Piece piece = new Piece(3, 2);
        Entity[] blocks = new Entity[4];
        for (int i = 0; i < 4; i++) {
            blocks[i] = TetrisEntityFactory.getBlock("Z-" + count + i, new Color(1, 0, 0), 1);
        }

        piece.addEntity(blocks[0], 0, 0);
        piece.addEntity(blocks[1], 1, 0);
        piece.addEntity(blocks[2], 1, 1);
        piece.addEntity(blocks[3], 2, 1);

        count += 4;

        return piece;
    }
}
