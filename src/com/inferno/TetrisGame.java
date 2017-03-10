package com.inferno;

import com.inferno.gui.*;
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
    private boolean restartGame = false;

    private String restartState = "Demo";

    private boolean bypassLock = false;
    private boolean advance = false;

    private float dropInterval = 1.0f; // Time in seconds that a piece takes to drop one level
    private float regularDropInterval = 1.0f;
    private Timer dropTimer;

    private Stopwatch lockTimer;
    private final float lockLimit = 0.25f;

    private int score = 0;

    private Stopwatch qtEvent;
    private TetrisGUI currentGUI;
    private LossGUI lossGUI;

    private float qtTimeLimit;

    private Stopwatch paywallTimer;

    private Timer adTimer;
    private Stopwatch adBlockCooldown;

    private boolean adBlockReady = true;

    private float adBlockTime = 2.5f;

    private final int DEMO_GOAL = 150;
    private final int CIRCLE_1_GOAL = 400;
    private final int CIRCLE_3_GOAL = 250;
    private final int CIRCLE_5_GOAL = 150;

    private int circle5PieceCount = 15;
    private int circle5PieceLimit = 15;

    public TetrisGame(int width, int height, float fps, boolean fullscreen) {
        super(width, height, "Six Circles of Bad Game Design", fps, fullscreen);
        this.world = new TetrisWorld(this, WIDTH, HEIGHT, TILE_SIZE);
    }

    public TetrisWorld getWorld() {
        return (TetrisWorld) world;
    }

    @Override
    public void initGame() {
        float ratio = (float) width / (float) height;
        GLRenderingEngine.setProjectionMatrix(MatrixUtils.getOrthographicProjectionMatrix(WORLD_SIZE, 0, 0, WORLD_SIZE * ratio, -1, 1));
        w = getWorld();
        w.setXOffset(8.33f);

        nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM);
        w.addPiece(nextPiece);

        dropTimer = new Timer(dropInterval, this::timerTick);

        lockTimer = new Stopwatch(lockLimit, () -> {}, this::lockTimer);

        gsm = new StateMachine(new State("Demo"));

        gsm.addPossibilities("Main Menu", "Demo", "Circle 1", "Circle 2", "Circle 3", "Circle 4", "Circle 4: Red", "Circle 4: Green", "Circle 4: Blue",
                "Circle 5", "Circle 6", "End Screen", "Loss");
        gsm.addTransition("*", "Demo", this::initDemo);
        gsm.addTransition("*", "Circle 1", this::initCircle1);
        gsm.addTransition("*", "Circle 2", this::initCircle2);
        gsm.addTransition("*", "Circle 3", this::initCircle3);
        gsm.addTransition("*", "Circle 4", this::initCircle4);
        gsm.addTransition("*", "Circle 4: Red", this::initCircle4Red);
        gsm.addTransition("*", "Circle 4: Green", this::initCircle4Green);
        gsm.addTransition("*", "Circle 4: Blue", this::initCircle4Blue);
        gsm.addTransition("*", "Circle 5", this::initCircle5);
        gsm.addTransition("*", "Circle 6", this::initCircle6);
        gsm.addTransition("*", "Loss", this::initLoss);

        initDemo();
    }

    private void lockTimer() {
        System.out.println("Lock timer ended.");
        nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM);
        resolveFilledRows();

        if (!w.addPieceIfValid(nextPiece)) {
            onLoss();
        }

        dropTimer.restart();
    }

    private void activateQuicktimeEvent(boolean bypassLock) {
        int roll = (int) (Math.random() * 6) + 1;
        if (qtEvent == null) {
            if (roll <= 3) {
                System.out.println(qtTimeLimit);
                qtEvent = new Stopwatch(qtTimeLimit, () -> {
                    quicktimeSuccess();
                    this.bypassLock = bypassLock;
                }, this::quicktimeFailure);
                qtEvent.start();

                ((FirstCircleGUI) currentGUI).activateQuicktimeEvent("Q", qtTimeLimit);

                qtTimeLimit -= 0.1 * qtTimeLimit;
            } else
                bypassQuicktime();
        }
    }

    private void lockTimerCircle1() {
        activateQuicktimeEvent(false);
        dropTimer.restart();
    }

    private void quicktimeSuccess() {
        nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM);
        resolveFilledRows();

        dropTimer.restart();
        if (!w.addPieceIfValid(nextPiece))
            onLoss();

        System.out.println("Quicktime Event Passed.");

        ((FirstCircleGUI)currentGUI).deactivateQuicktimeEvent();

        float percentComplete = qtEvent.getTime() / qtEvent.getTimeLimit();

        String qtMsg;
        if (percentComplete >= 0 && percentComplete <= 0.25f)
            qtMsg = "Great!";
        else if (percentComplete >= 0.25f && percentComplete <= 0.75f)
            qtMsg = "Good!";
        else
            qtMsg = "Close!";

        ((FirstCircleGUI)currentGUI).passQuicktimeEvent(qtMsg);

        qtEvent = null;

        regularDropInterval -= 0.025 * regularDropInterval;
        dropInterval -= 0.025 * dropInterval;
        dropTimer.setInterval(dropInterval);
    }

    private void bypassQuicktime() {
        nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM);
        resolveFilledRows();

        dropTimer.restart();
        if (!w.addPieceIfValid(nextPiece))
            onLoss();

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
        ((FirstCircleGUI)currentGUI).failQuicktimeEvent();

        dropTimer.restart();

        qtEvent = null;
    }

    private void initDemo() {
        gameLost = false;

        w.clearAll();
        dropTimer.start();

        renderingEngine.setClearColor(new Color(0.1f, 0.1f, 0.1f, 1));
        if (currentGUI != null)
            removeGUI(currentGUI);
        addGUI(currentGUI = new IntroGUI(this, width, height, WORLD_SIZE, DEMO_GOAL));

        score = 0;
        currentGUI.resetScore();
    }

    private void initCircle1() {
        gameLost = false;
        System.out.println("Entering Circle 1.");

        w.clearAll();

        renderingEngine.setClearColor(0.05f, 0, 0, 0);

        qtTimeLimit = 2.5f;

        dropInterval = 0.75f;
        regularDropInterval = 0.75f;
        dropTimer.setInterval(dropInterval);
//        dropTimer.setAction(this::timerTickCircle1);
        lockTimer.setStoppedAction(this::lockTimerCircle1);

        removeGUI(currentGUI);
        currentGUI = new FirstCircleGUI(this, width, height, WORLD_SIZE, CIRCLE_1_GOAL);
        addGUI(currentGUI);

        score = 0;
        currentGUI.resetScore();
    }

    private void initCircle2() {
        gameLost = false;
        System.out.println("Entering Circle 2.");

        w.clearAll();

        renderingEngine.setClearColor(0.1f, 0, 0, 0);

        dropTimer.stop();

        paywallTimer = new Stopwatch(5.0f, () -> {}, () -> {
            removeGUI(currentGUI);
            currentGUI = new SecondCircleGUI(this, width, height, WORLD_SIZE);
            addGUI(currentGUI);

            System.out.println("Started the paywall.");
        });

        paywallTimer.start();

        score = 0;
        currentGUI.resetScore();
    }

    // TODO: Complete circles
    private void initCircle3() {
        gameLost = false;
        removeGUI(currentGUI);
        currentGUI = new ThirdCircleGUI(this, width, height, WORLD_SIZE, adBlockTime, CIRCLE_3_GOAL);
        addGUI(currentGUI);

        renderingEngine.setClearColor(0, 0, 0, 0);

        w.clearAll();

        System.out.println("Entering Circle 3.");

        dropInterval = 0.3f;
        regularDropInterval = dropInterval;
        dropTimer.setInterval(dropInterval);
        lockTimer.setStoppedAction(this::lockTimer);
        dropTimer.start();

        adTimer = new Timer(7.5f, w::addAdRow);
        adTimer.start();

        adBlockCooldown = new Stopwatch(adBlockTime, () -> {}, this::restoreAdBlock);

        score = 0;
        currentGUI.resetScore();
    }

    private void initCircle4() {
        gameLost = false;
        System.out.println("Entering Circle 4.");

        renderingEngine.setClearColor(0.4f, 0, 0, 0);

        w.clearAll();
        dropTimer.stop();

        removeGUI(currentGUI);
        currentGUI = new FourthCircleGUI(this, width, height, WORLD_SIZE);
        addGUI(currentGUI);

        score = 0;
        currentGUI.resetScore();
    }

    private void initCircle5() {
        gameLost = false;
        renderingEngine.setClearColor(0.65f, 0, 0, 0);

        w.clearAll();
        circle5PieceLimit = 15;
        circle5PieceCount = circle5PieceLimit;
        lockTimer.setStoppedAction(this::lockTimerCircle5);
        dropTimer.start();

        removeGUI(currentGUI);
        currentGUI = new FifthCircleGUI(this, width, height, WORLD_SIZE, CIRCLE_5_GOAL, circle5PieceLimit);
        addGUI(currentGUI);

        score = 0;
        currentGUI.resetScore();
    }

    private void initCircle6() {
        gameLost = false;
        renderingEngine.setClearColor(1, 0, 0, 0);

        w.clearAll();

        removeGUI(currentGUI);
        currentGUI = new SixthCircleGUI(this, width, height, WORLD_SIZE);
        addGUI(currentGUI);

        score = 0;
        currentGUI.resetScore();
    }

    private void initCircle4Red() {
        gameLost = false;
        removeGUI(currentGUI);
        currentGUI = new FourthCircleRedGUI(this, width, height, WORLD_SIZE);
        addGUI(currentGUI);
    }

    private void initCircle4Green() {
        gameLost = false;
        removeGUI(currentGUI);
        currentGUI = new FourthCircleGreenGUI(this, width, height, WORLD_SIZE);
        addGUI(currentGUI);
    }

    private void initCircle4Blue() {
        gameLost = false;
        removeGUI(currentGUI);
        currentGUI = new FourthCircleBlueGUI(this, width, height, WORLD_SIZE);
        addGUI(currentGUI);
    }

    private void initLoss() {
        addGUI(lossGUI = new LossGUI(this, gsm.getCurrentStateName()));
    }

    private void useAdBlock() {
        w.removeAllAds();
        adBlockCooldown = new Stopwatch(adBlockTime, () -> {}, this::restoreAdBlock);
        adBlockCooldown.start();

        ((ThirdCircleGUI)currentGUI).setOnCooldown();

        adBlockReady = false;
    }

    private void restoreAdBlock() {
        adBlockReady = true;
        ((ThirdCircleGUI)currentGUI).setOffCooldown();
    }

    @Override
    public void preGameUpdate() {

    }

    @Override
    public void fixedGameUpdate(float dt) {
        dropTimer.step(dt);
        lockTimer.tick(dt);

        if (restartGame) {
            restartWithState(restartState);
        }

        switch (gsm.getCurrentStateName()) {
            case "Loss":
                break;
            case "Demo":
                updateDemo(dt);
                break;
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

    public void restartWithState(String state) {
        gameLost = false;
        removeGUI(lossGUI);
        gsm.changeState(state);
        dropTimer.start();

        restartGame = false;
    }

    private void updateDemo(float dt) {
        if (score >= DEMO_GOAL) {
            advance = true;
            gsm.changeState("Circle 1");
        }
    }

    private void updateCircle1(float dt) {
        if (qtEvent != null) {
            ((FirstCircleGUI)currentGUI).updateTimer(qtEvent.getTimeLimit() - qtEvent.getTime());
            qtEvent.tick(dt);
        }

        if (score >= CIRCLE_1_GOAL) {
            advance = true;
            gsm.changeState("Circle 2");
        }
    }

    private void updateCircle2(float dt) {
        paywallTimer.tick(dt);
    }

    private void updateCircle3(float dt) {
        adTimer.step(dt);

        if (adBlockCooldown != null)
            adBlockCooldown.tick(dt);

        if (adBlockReady && Keyboard.isKeyReleased(GLFW.GLFW_KEY_B)) {
            useAdBlock();
        }

        if (!adBlockReady) {
            ((ThirdCircleGUI)currentGUI).updateCooldown(adBlockCooldown.getTime());
        }

        if (score >= CIRCLE_3_GOAL) {
            advance = true;
            gsm.changeState("Circle 4");
        }
    }

    private void updateCircle4(float dt) {}
    private void updateCircle5(float dt) {}
    private void updateCircle6(float dt) {}

    @Override
    public void postGameUpdate() {

    }

    private void timerTick() {
        if (!w.moveIfValid(nextPiece, 0, 1)) {
            if (!bypassLock && !advance) {
                dropTimer.stop();
                lockTimer.restart();
            } else {
                bypassLock = false;
                advance = false;
            }
        }
    }

    private void timerTickCircle5() {
        if (!w.moveIfValid(nextPiece, 0, 1)) {
            if (!bypassLock) {
                dropTimer.stop();
                lockTimer.restart();
            } else {
                bypassLock = false;
                decrementPieces();
            }
        }
    }

    private void decrementPieces() {
        circle5PieceCount--;
        ((FifthCircleGUI)currentGUI).updatePiecesLeft(circle5PieceCount, circle5PieceLimit);

        if (circle5PieceCount <= 0)
            onLoss();
    }

    private void lockTimerCircle5() {
        nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM);
        resolveFilledColumns();

        while (!w.addPieceIfValid(nextPiece)) {
            nextPiece.translate(1, 0);
        }

        decrementPieces();
        dropTimer.restart();
    }

    private void onLoss() {
        System.out.println("You lose! Score: " + score);
        gameLost = true;
        gsm.changeState("Loss");
        dropTimer.stop();
    }

    public void signalRestart(String state) {
        restartGame = true;
        restartState = state;
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
            currentGUI.updateScore(points, filledRows.size());

            w.fillEmptyRows(filledRows);
        }
    }

    private void resolveFilledColumns() {
        List<Integer> filledColumns = w.checkForFilledColumns();

        if (filledColumns.size() > 0) {
            for (int col : filledColumns) {
                System.out.println("Column " + col + " filled.");
                w.clearColumn(col);
            }

            int points = (50 * filledColumns.size()) + ((filledColumns.size() - 1) * 10);
            score += points;
            System.out.println("Scored " + points + " points! New Score: " + score);
            currentGUI.updateScore(points, filledColumns.size());

            circle5PieceCount = circle5PieceLimit;
            ((FifthCircleGUI)currentGUI).updatePiecesLeft(circle5PieceCount, circle5PieceLimit);
        }
    }

    public StateMachine getStateMachine() {
        return this.gsm;
    }

    @Override
    public void handleInput() {
        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_LEFT) && !gameLost) {
            w.moveIfValid(nextPiece, -1, 0);
            if (lockTimer.isRunning() && !lockTimer.isStopped())
                lockTimer.restart();
        }

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_RIGHT) && !gameLost) {
            w.moveIfValid(nextPiece, 1, 0);
            if (lockTimer.isRunning() && !lockTimer.isStopped())
                lockTimer.restart();
        }

        if ((Keyboard.isKeyPressed(GLFW.GLFW_KEY_UP) || Keyboard.isKeyPressed(GLFW.GLFW_KEY_X)) && !gameLost) {
            Piece newPiece = nextPiece.rotateRight();
            if (w.moveIfValid(nextPiece, newPiece))
                nextPiece = newPiece;
            if (lockTimer.isRunning() && !lockTimer.isStopped())
                lockTimer.restart();
        }

        if ((Keyboard.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL) || Keyboard.isKeyPressed(GLFW.GLFW_KEY_Z)) && !gameLost) {
            Piece newPiece = nextPiece.rotateLeft();
            if (w.moveIfValid(nextPiece, newPiece))
                nextPiece = newPiece;
            if (lockTimer.isRunning() && !lockTimer.isStopped())
                lockTimer.restart();
        }

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_SPACE) && !gameLost) {
            w.moveToBottom(nextPiece);

            if (gsm.getCurrentStateName().equals("Circle 1")) {
                activateQuicktimeEvent(true);
            } else if (!gsm.getCurrentStateName().equals("Circle 5")) {
                dropTimer.stop();
                nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM);
                resolveFilledRows();

                dropTimer.restart();

                if (!w.addPieceIfValid(nextPiece)) {
                    onLoss();
                }

                if (gsm.getCurrentStateName().equals("Circle 5"))
                    decrementPieces();
            } else if (gsm.getCurrentStateName().equals("Circle 5")) {
                dropTimer.stop();
                nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM);
                resolveFilledRows();

                while (!w.addPieceIfValid(nextPiece)) {
                    nextPiece.translate(1, 0);
                }

                dropTimer.restart();

                decrementPieces();
            }

            bypassLock = true;
        }

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_DOWN) && !gameLost) {
            dropInterval = 0.05f;
            dropTimer.setInterval(dropInterval);
        }

        if (Keyboard.isKeyReleased(GLFW.GLFW_KEY_DOWN) && !gameLost) {
            dropInterval = regularDropInterval;
            dropTimer.setInterval(dropInterval);
        }

        if (Keyboard.isKeyReleased(GLFW.GLFW_KEY_1)) {
            gsm.changeState("Circle 1");
            gameLost = false;
        }

        if (Keyboard.isKeyReleased(GLFW.GLFW_KEY_2)) {
            gsm.changeState("Circle 2");
            gameLost = false;
        }

        if (Keyboard.isKeyReleased(GLFW.GLFW_KEY_3)) {
            gsm.changeState("Circle 3");
            gameLost = false;
        }

        if (Keyboard.isKeyReleased(GLFW.GLFW_KEY_4)) {
            gsm.changeState("Circle 4");
            gameLost = false;
        }

        if (Keyboard.isKeyReleased(GLFW.GLFW_KEY_5)) {
            gsm.changeState("Circle 5");
            gameLost = false;
        }

        if (Keyboard.isKeyReleased(GLFW.GLFW_KEY_6)) {
            gsm.changeState("Circle 6");
            gameLost = false;
        }

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_Q) && qtEvent != null && !qtEvent.isStopped() && gsm.isCurrentState("Circle 1")) {
            qtEvent.interrupt();
        }

        if (Keyboard.isKeyReleased(GLFW.GLFW_KEY_L)) {
            onLoss();
        }
    }
}
