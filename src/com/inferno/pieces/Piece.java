package com.inferno.pieces;

import com.morph.engine.entities.EntityGrid;
import com.morph.engine.math.Vector2f;

import java.util.ArrayList;

/**
 * Created by Fernando on 2/11/2017.
 */
public class Piece extends EntityGrid {
    private int x;
    private int y;

    public Piece(int width, int height) {
        super(width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2f getPosition() {
        return new Vector2f(x, y);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * WARNING: X and Y will be truncated!
     */
    public void setPosition(Vector2f position) {
        this.x = (int) position.getX();
        this.y = (int) position.getY();
    }

    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public int[][] getBlockLocations() {
        return getBlockLocationsWithOffset(0, 0);
    }

    public int[][] getBlockLocationsWithOffset(int dx, int dy) {
        ArrayList<Integer[]> result = new ArrayList<>();

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (getEntity(x, y) != null)
                    result.add(new Integer[]{this.x + x + dx, this.y + y + dy});
            }
        }

        Integer[][] finalResult = new Integer[result.size()][2];
        result.toArray(finalResult);

        int[][] resultCasted = new int[result.size()][2];

        for (int i = 0; i < finalResult.length; i++) {
            resultCasted[i][0] = finalResult[i][0];
            resultCasted[i][1] = finalResult[i][1];
        }

        return resultCasted;
    }

    public int[][] getExposedBlockLocationsWithOffset(int dx, int dy) {
        ArrayList<Integer[]> result = new ArrayList<>();

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (getEntity(x, y) != null && getEntity(x, y+1) == null)
                    result.add(new Integer[]{this.x + x + dx, this.y + y + dy});
            }
        }

        Integer[][] finalResult = new Integer[result.size()][2];
        result.toArray(finalResult);

        int[][] resultCasted = new int[result.size()][2];

        for (int i = 0; i < finalResult.length; i++) {
            resultCasted[i][0] = finalResult[i][0];
            resultCasted[i][1] = finalResult[i][1];
        }

        return resultCasted;
    }

    public Piece rotateLeft() {
        Piece newPiece = new Piece(getHeight(), getWidth());
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                newPiece.addEntity(this.getEntity(x, y), y, getWidth() - x - 1);
            }
        }

        newPiece.setPosition(x, y);
        return newPiece;
    }

    public Piece rotateRight() {
        Piece newPiece = new Piece(getHeight(), getWidth());
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                newPiece.addEntity(this.getEntity(x, y), getHeight() - y - 1, x);
            }
        }

        newPiece.setPosition(x, y);
        return newPiece;
    }
}
