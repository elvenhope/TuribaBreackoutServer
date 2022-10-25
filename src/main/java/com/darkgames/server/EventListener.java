package com.darkgames.server;

import Packets.*;

import java.util.*;

public class EventListener {
    public Timer t;
    Random generator = new Random();

    public void received (Object p, Connection connection) {
        if (p instanceof AddConnectionPacket) {

            AddConnectionPacket packet = (AddConnectionPacket) p;
            LobbyConnectionListPacket packet2 = new LobbyConnectionListPacket();
            IdentityPacket IdPacket = new IdentityPacket();
            BallCordsPacket BallPacket = new BallCordsPacket(
                    ConnectionHandler.BallX, ConnectionHandler.BallY, ConnectionHandler.Xdir, ConnectionHandler.Ydir);

            packet.id = connection.id;

            IdPacket.id = connection.id;
            connection.sendObject(IdPacket);

            ConnectionHandler.addEmptyPaddle(packet.id);

            if(ConnectionHandler.scoreTracker.get(connection.id) == null) {
                ConnectionHandler.scoreTracker.put(connection.id, 0);
            }

            AllScoresPacket packet4 = new AllScoresPacket();
            packet4.scoreTracker.clear();

            for (HashMap.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                Integer key = entry.getKey();
                Connection value = entry.getValue();
                packet2.addUsers(value.id);
                packet4.scoreTracker.put(value.id, ConnectionHandler.scoreTracker.get(value.id));
            }

            for (HashMap.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                Integer key = entry.getKey();
                Connection value = entry.getValue();
                if (value != null) {
                    value.sendObject(packet2);
                    value.sendObject(new PaddlesPacket(ConnectionHandler.paddles));
                    value.sendObject(BallPacket);
                    value.sendObject(packet4);
                    if (value != connection) {
                        value.sendObject(packet);
                    }
                }
            }
            System.out.println(Objects.equals(ConnectionHandler.gameState, "Not Started"));
            if(ConnectionHandler.connections.size() == 3 && Objects.equals(ConnectionHandler.gameState, "Not Started")) {
                ConnectionHandler.gameState = "Started";
                GameStatePacket gamePacket = new GameStatePacket("Started");
                for (HashMap.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                    Integer key = entry.getKey();
                    Connection value = entry.getValue();
                    value.sendObject(gamePacket);
                }
                t = new Timer();
                t.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        PaddleSwitchPacket packet = new PaddleSwitchPacket();
                        for (HashMap.Entry<Integer, Integer> entry : ConnectionHandler.paddles.entrySet()) {
                            Integer key = entry.getKey();
                            Integer payloadValue;
                            if(ConnectionHandler.paddlesOrder.get(key) == null) {
                                payloadValue = key + 1;
                            } else {
                                payloadValue = ConnectionHandler.paddlesOrder.get(key) + 1;
                            }
                            if(payloadValue >= ConnectionHandler.paddles.size()) {
                                payloadValue = 0;
                            }
                            packet.paddles.put(key, payloadValue);
                            ConnectionHandler.paddlesOrder.put(key, payloadValue);
                        }
                        for (HashMap.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                            Integer key = entry.getKey();
                            Connection value = entry.getValue();
                            value.sendObject(packet);
                        }
                        System.out.println(packet.paddles);
                    }
                }, 0,10000);
                System.out.println("Timer Started");
            }
        } else if (p instanceof RemoveConnectionPacket) {
            RemoveConnectionPacket packet = (RemoveConnectionPacket) p;
            if(ConnectionHandler.connections.get(packet.id) != null) {
//                System.out.println("Connection: " + packet.id + " has disconnected");
                ConnectionHandler.connections.get(packet.id).close();
                ConnectionHandler.connections.remove(packet.id);
                ConnectionHandler.scoreTracker.remove(packet.id);
//                System.out.println(ConnectionHandler.connections);
                LobbyConnectionListPacket packet2 = new LobbyConnectionListPacket();
                for (HashMap.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                    Integer key = entry.getKey();
                    Connection value = entry.getValue();
                    packet2.addUsers(value.id);
                }
                for (HashMap.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                    Integer key = entry.getKey();
                    Connection value = entry.getValue();
                    if (value != null) {
                        value.sendObject(packet);
                        value.sendObject(packet2);
                    }
                }
            }
        } else if(p instanceof SinglePaddlePacket) {
            SinglePaddlePacket packet = (SinglePaddlePacket) p;
            int id = packet.id;
            int dx = packet.dx;
            ConnectionHandler.paddles.put(id, dx);
//            System.out.println(ConnectionHandler.paddles + " ID:" + id);
            for (HashMap.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                Integer key = entry.getKey();
                Connection value = entry.getValue();
                PaddlesPacket sendingPacket = new PaddlesPacket(ConnectionHandler.paddles);
//                System.out.println(sendingPacket.paddles);
                value.sendObject(sendingPacket);
            }
        } else if(p instanceof DestroyedBrickPacket) {
            DestroyedBrickPacket packet = (DestroyedBrickPacket) p;
            ConnectionHandler.brickTracker.put(packet.id, true);
        } else if(p instanceof BallCordsPacket) {
            BallCordsPacket packet = (BallCordsPacket) p;
            ConnectionHandler.Update(packet.BallX, packet.BallY, packet.Xdir, packet.Ydir);
            BallCordsPacket sendingPacket = new BallCordsPacket(
                    ConnectionHandler.BallX, ConnectionHandler.BallY, ConnectionHandler.Xdir, ConnectionHandler.Ydir
            );
            brickTrackerPacket sendingPacket2 = new brickTrackerPacket(ConnectionHandler.brickTracker);
            AllScoresPacket packet2 = new AllScoresPacket();
            for (HashMap.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                Integer key = entry.getKey();
                Connection value = entry.getValue();
                packet2.scoreTracker.put(value.id, ConnectionHandler.scoreTracker.get(value.id));
            }
            for (HashMap.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                Integer key = entry.getKey();
                Connection value = entry.getValue();
                if(value != connection) {
                    value.sendObject(sendingPacket);
                    value.sendObject(sendingPacket2);
                }
                value.sendObject(packet2);
            }
        } else if(p instanceof ScoreboardPacket) {
            ScoreboardPacket packet = (ScoreboardPacket) p;
            if(ConnectionHandler.scoreTracker.get(packet.id) == null) {
                ConnectionHandler.scoreTracker.put(packet.id, packet.score);
            } else {
                int currentScore = ConnectionHandler.scoreTracker.get(packet.id);
                ConnectionHandler.scoreTracker.put(packet.id, currentScore + packet.score);
            }
        } else if(p instanceof GameStatePacket) {
            GameStatePacket packet = (GameStatePacket) p;
            if(Objects.equals(packet.State, "Finished")){
                AllScoresPacket packet2 = new AllScoresPacket();
                for (HashMap.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                    Integer key = entry.getKey();
                    Connection value = entry.getValue();
                    packet2.scoreTracker.put(value.id, ConnectionHandler.scoreTracker.get(value.id));
                }
                for (HashMap.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                    Integer key = entry.getKey();
                    Connection value = entry.getValue();
                    if(value != connection) {
                        value.sendObject(packet2);
                    }
                }
                t.cancel();
                ConnectionHandler.ResetState();
            }
        }
    }
}
