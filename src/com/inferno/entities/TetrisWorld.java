package com.inferno.entities;

import com.inferno.entities.components.Ad;
import com.morph.engine.core.Game;
import com.morph.engine.core.TileWorld;
import com.morph.engine.entities.Entity;
import com.inferno.pieces.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Fernando on 2/11/2017.
 */
public class TetrisWorld extends TileWorld {
    private List<Piece> pieces;

    public TetrisWorld(Game game, int width, int height, float tileSize) {
        super(game, width, height, tileSize);
        this.pieces = new ArrayList<>();
    }

    public void clearAll() {
        pieces.clear();

        for (Entity e : this.getEntities()) {
            removeEntity(e);
        }
    }

    public boolean addPiece(Piece p) {
        pieces.add(p);
        return addEntityGrid(p, p.getX(), p.getY());
    }

    public boolean removePiece(Piece p) {
        pieces.remove(p);
        for (int y = 0; y < p.getHeight(); y++) {
            for (int x = 0; x < p.getWidth(); x++) {
                if (p.getEntity(x, y) != null)
                    if (!removeEntity(x + p.getX(), y + p.getY()))
                        return false;
            }
        }

        return true;
    }

    public boolean translatePiece(Piece p, int dx, int dy) {
        if (!pieces.contains(p))
            return false;

        boolean success = true;
        lazyMoveEntityGrid(p, p.getX() + dx, p.getY() + dy);

        p.translate(dx, dy);

        return success;
    }

    public boolean moveIfValid(Piece p, int dx, int dy) {
        removePiece(p);
        p.translate(dx, dy);
        if (this.areEmpty(p.getBlockLocations())) {
            addPiece(p);
            return true;
        }
        p.translate(-dx, -dy);
        addPiece(p);

        return false;
    }

    public boolean moveIfValid(Piece p, Piece newP) {
        removePiece(p);
        if (this.areEmpty(newP.getBlockLocations())) {
            addPiece(newP);
            return true;
        }
        addPiece(p);

        return false;
    }

    public void moveToBottom(Piece p) {
        removePiece(p);
        while (areEmpty(p.getBlockLocations())) {
            p.translate(0, 1);
        }
        p.translate(0, -1);
        addPiece(p);
    }

    public List<Integer> checkForFilledRows() {
        List<Integer> filledRows = IntStream.range(0, getHeight()).boxed().collect(Collectors.toList());

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (getEntity(x, y) == null || getEntity(x, y).hasComponent(Ad.class)) {
                    filledRows.remove((Integer)y);
                    break;
                }
            }
        }

        return filledRows;
    }

    public List<Integer> checkForEmptyRows() {
        List<Integer> filledRows = IntStream.range(0, getHeight()).boxed().collect(Collectors.toList());

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (getEntity(x, y) != null) {
                    filledRows.remove((Integer)y);
                    break;
                }
            }
        }

        return filledRows;
    }

    public List<Integer> checkForEmptyRows(int startRow) {
        List<Integer> filledRows = IntStream.range(startRow, getHeight()).boxed().collect(Collectors.toList());

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (getEntity(x, y) != null) {
                    filledRows.remove((Integer)y);
                    break;
                }
            }
        }

        return filledRows;
    }

    public void clearRow(int row) {
        for (int i = 0; i < getWidth(); i++) {
            removeEntity(i, row);
        }
    }

    public void lowerAllPieces(int offset) {
        for (Entity e : getEntities()) {
            translateEntity(e, 0, offset);
        }
    }

    public void fillEmptyRows() {
        List<Integer> emptyRows = checkForEmptyRows();
        fillEmptyRows(emptyRows);
    }

    public void fillEmptyRows(List<Integer> emptyRows) {
        List<Integer> remaining = emptyRows;

        while (remaining.size() > 0) {
            for (int row : emptyRows) {
                int newRow = row - 1;
                for (int y = newRow; y >= 0; y--) {
                    for (int x = 0; x < getWidth(); x++) {
                        if (getEntity(x, y) != null)
                            translateEntity(x, y, 0, 1);
                    }
                }
            }

            remaining = checkForEmptyRows(emptyRows.get(0));
        }
    }

    public boolean anyFilledColumns() {
        for (int x = 0; x < getWidth(); x++) {
            if (getEntity(x, 0) != null) {
                return true;
            }
        }

        return false;
    }

    public void addAdRow() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = getHeight(); y >= 0; y--) {
                if (isEmpty(x, y)) {
                    addEntity(TetrisEntityFactory.getAdBlock(1), x, y);
                    break;
                }
            }
        }
    }

    public void removeAllAds() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = getHeight(); y >= 0; y--) {
                if (getEntity(x, y) != null && getEntity(x, y).hasComponent(Ad.class)) {
                    removeEntity(x, y);
                }
            }
        }
    }
}
