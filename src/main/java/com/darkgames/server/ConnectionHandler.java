package com.darkgames.server;

import java.util.HashMap;

public class ConnectionHandler {
    public static HashMap<Integer, Connection> connections = new HashMap<>();

    public static HashMap<Integer, Integer> paddles = new HashMap<>();
    public static HashMap<Integer, Integer> paddlesOrder = new HashMap<>();
    public static HashMap<Integer, Boolean> brickTracker = new HashMap<>();
    public static HashMap<Integer, Integer> scoreTracker = new HashMap<>();
    public static int BallX;
    public static int BallY;
    public static int Xdir;
    public static int Ydir;

    public static String gameState = "Not Started";

    public static void addEmptyPaddle(int id) {
        paddles.put(id, 0);
    }

    public static void Update(int NewBallX, int NewBallY, int NewXdir, int NewYdir) {
        BallX = NewBallX;
        BallY = NewBallY;
        Xdir = NewXdir;
        Ydir = NewYdir;
    }

    public static void ResetState() {
        paddles.clear();
        brickTracker.clear();
        scoreTracker.clear();
        connections.clear();
        Main.server.shutdown();
    }
}
