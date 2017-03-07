package com.inferno;

import com.inferno.gui.FirstCircleGUI;
import com.inferno.gui.IntroGUI;
import com.inferno.gui.SecondCircleGUI;
import com.inferno.gui.TetrisGUI;
import com.morph.engine.util.State;
import com.morph.engine.util.StateMachine;
import com.inferno.util.Stopwatch;
import com.morph.engine.core.Game;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.GLRenderingEngine;
import com.morph.engine.input.Keyboard;
import com.morph.engine.math.MatrixUtils;
import com.morph.engine.util.Timer;
import com.inferno.entities.TetrisWorld;
import com.inferno.pieces.Piece;
import com.inferno.pieces.PieceFactory;
import org.lwjgl.glfw.GLFW;

import java.util.List;

/**
 * Created by Fernando on 2/10/2017.
 */
public class TetrisGame extends Game {
    private static final int WIDTH = 10, HEIGHT = 20;
    private static final float TILE_SIZE = 1;
    private static final int WORLD_SIZE = 20;
    private Piece nextPiece;
    private TetrisWorld w;

    private StateMachine gsm;

    private boolean gameLost = false;

    private float dropInterval = 1.0f; // Time in seconds that a piece takes to drop one level
    private float regularDropInterval = 1.0f;
    private Timer dropTimer;

    private int score = 0;

    private Stopwatch qtEvent;
    private TetrisGUI currentGUI;

    private float qtTimeLimit = 2.5f;

    private Timer paywallTimer;

    public TetrisGame(int width, int height, float fps, boolean fullscreen) {
        super(width, height, "Six Circles of Bad Game Design", fps, fullscreen);
        this.world = new TetrisWorld(this, WIDTH, HEIGHT, TILE_SIZE);
    }

    public TetrisWorld getWorld() {
        return (TetrisWorld) world;
    }

    @Override
    public void initGame() {
        renderingEngine.setClearColor(new Color(0.1f, 0.1f, 0.1f, 1));
        float ratio = (float) width / (float) height;
        GLRenderingEngine.setProjectionMatrix(MatrixUtils.getOrthographicProjectionMatrix(WORLD_SIZE, 0, 0, WORLD_SIZE * ratio, -1, 1));
        w = getWorld();
        w.setXOffset(8.33f);

        nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM);
        w.addPiece(nextPiece);

        dropTimer = new Timer(dropInterval, this::timerTick);
        dropTimer.start();

        float panelWidth = 12.5f * WORLD_SIZE;

        addGUI(currentGUI = new IntroGUI(this, width, height, WORLD_SIZE));

        gsm = new StateMachine(new State("Demo"));

        gsm.addPossibilities("Main Menu", "Demo", "Circle 1", "Circle 2", "Circle 3", "Circle 4", "Circle 5", "Circle 6", "End Screen");
        gsm.addTransition("*", "Circle 1", this::initCircle1);
        gsm.addTransition("*", "Circle 2", this::initCircle2);
        gsm.addTransition("*", "Circle 3", this::initCircle3);
        gsm.addTransition("*", "Circle 4", this::initCircle4);
        gsm.addTransition("*", "Circle 5", this::initCircle5);
        gsm.addTransition("*", "Circle 6", this::initCircle6);
    }

    private void timerTickCircle1() {
        if (!w.moveIfValid(nextPiece, 0, 1)) {
            int roll = (int) (Math.random() * 6) + 1;
            if (qtEvent == null) {
                if (roll <= 3) {
                    System.out.println(qtTimeLimit);
                    qtEvent = new Stopwatch(qtTimeLimit, this::quicktimeSuccess, this::quicktimeFailure);
                    qtEvent.start();

                    ((FirstCircleGUI) currentGUI).activateQuicktimeEvent("Q", qtTimeLimit);

                    qtTimeLimit -= 0.05 * qtTimeLimit;
                } else
                    bypassQuicktime();
            }
        }
    }

    private void quicktimeSuccess() {
        nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM);
        resolveFilledRows();

        if (w.anyFilledColumns()) {
            System.out.println("You lose! Score: " + score);
            gameLost = true;
            dropTimer.stop();
        } else
            w.addPiece(nextPiece);

        System.out.println("Quicktime Event Passed.");

        ((FirstCircleGUI)currentGUI).deactivateQuicktimeEvent();

        qtEvent = null;

        regularDropInterval -= 0.025 * regularDropInterval;
        dropInterval -= 0.025 * dropInterval;
        dropTimer.setInterval(dropInterval);
    }

    private void bypassQuicktime() {
        nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM);
        resolveFilledRows();

        if (w.anyFilledColumns()) {
            System.out.println("You lose! Score: " + score);
            gameLost = true;
            dropTimer.stop();
        } else
            w.addPiece(nextPiece);

        System.out.println("Quicktime Event Bypassed.");

        regularDropInterval -= 0.025 * regularDropInterval;
        dropInterval -= 0.025 * dropInterval;
        dropTimer.setInterval(dropInterval);
    }

    private void quicktimeFailure() {
        System.out.println("Quicktime Event Failed.");

        w.removePiece(nextPiece);
        nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM);
        w.addPiece(nextPiece);

        ((FirstCircleGUI)currentGUI).deactivateQuicktimeEvent();

        qtEvent = null;
    }

    private void initCircle1() {
        System.out.println("Entering Circle 1.");

        w.clearAll();

        dropInterval = 0.75f;
        regularDropInterval = 0.75f;
        dropTimer.setInterval(dropInterval);
        dropTimer.setAction(this::timerTickCircle1);

        removeGUI(currentGUI);
        currentGUI = new FirstCircleGUI(this, width, height, WORLD_SIZE);
        addGUI(currentGUI);
    }

    private void initCircle2() {
        System.out.println("Entering Circle 2.");

        w.clearAll();

        dropTimer.stop();

        paywallTimer = new Timer(5.0f, () -> {
            removeGUI(currentGUI);
            currentGUI = new SecondCircleGUI(this, width, height, WORLD_SIZE);
            addGUI(currentGUI);

            System.out.println("Started the paywall.");
        });

        paywallTimer.start();
    }

    // TODO: Complete circles
    private void initCircle3() {}
    private void initCircle4() {}
    private void initCircle5() {}
    private void initCircle6() {}

    @Override
    public void preGameUpdate() {

    }

    @Override
    public void fixedGameUpdate(float dt) {
        dropTimer.step(dt);

        switch (gsm.getCurrentStateName()) {
            case "Circle 1":
                updateCircle1(dt);
                break;
            case "Circle 2":
                updateCircle2(dt);
                break;
            case "Circle 3":
                updateCircle3(dt);
                break;
            case "Circle 4":
                updateCircle4(dt);
                break;
            case "Circle 5":
                updateCircle5(dt);
                break;
            case "Circle 6":
                updateCircle6(dt);
                break;
        }
    }

    private void updateCircle1(float dt) {
        if (qtEvent != null) {
            ((FirstCircleGUI)currentGUI).updateTimer(qtEvent.getTimeLimit() - qtEvent.getTime());
            qtEvent.tick(dt);
        }

        if (score >= 400) {
            gsm.changeState("Circle 2");
        }
    }

    private void updateCircle2(float dt) {
        paywallTimer.step(dt);
    }

    private void updateCircle3(float dt) {}
    private void updateCircle4(float dt) {}
    private void updateCircle5(float dt) {}
    private void updateCircle6(float dt) {}

    @Override
    public void postGameUpdate() {

    }

    private void timerTick() {
        if (!w.moveIfValid(nextPiece, 0, 1)) {
            nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM);
            resolveFilledRows();

            if (w.anyFilledColumns()) {
                System.out.println("You lose! Score: " + score);
                gameLost = true;
                dropTimer.stop();
            } else
                w.addPiece(nextPiece);
        }
    }

    private void resolveFilledRows() {
        List<Integer> filledRows = w.checkForFilledRows();

        if (filledRows.size() > 0) {
            for (int row : filledRows) {
                w.clearRow(row);
            }

            int points = (50 * filledRows.size()) + ((filledRows.size() - 1) * 10);
            score += points;
            System.out.println("Scored " + points + " points! New Score: " + score);
            currentGUI.updateScore(score);

            w.fillEmptyRows(filledRows);
        }
    }

    @Override
    public void handleInput() {
        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_A) && !gameLost) {
            w.moveIfValid(nextPiece, -1, 0);
        }

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_D) && !gameLost) {
            w.moveIfValid(nextPiece, 1, 0);
        }

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_LEFT) && !gameLost) {
            Piece newPiece = nextPiece.rotateLeft();
            if (w.moveIfValid(nextPiece, newPiece))
                nextPiece = newPiece;
        }

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_RIGHT) && !gameLost) {
            Piece newPiece = nextPiece.rotateRight();
            if (w.moveIfValid(nextPiece, newPiece))
                nextPiece = newPiece;
        }

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_SPACE) && !gameLost) {
            w.moveToBottom(nextPiece);
        }

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_S) && !gameLost) {
            dropInterval = 0.05f;
            dropTimer.setInterval(dropInterval);
        }

        if (Keyboard.isKeyReleased(GLFW.GLFW_KEY_S) && !gameLost) {
            dropInterval = regularDropInterval;
            dropTimer.setInterval(dropInterval);
        }

        if (Keyboard.isKeyReleased(GLFW.GLFW_KEY_1)) {
            gsm.changeState("Circle 1");
        }

        if (Keyboard.isKeyReleased(GLFW.GLFW_KEY_2)) {
            gsm.changeState("Circle 2");
        }

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_Q) && qtEvent != null && !qtEvent.isStopped() && gsm.isCurrentState("Circle 1")) {
            qtEvent.interrupt();
        }
    }
}
