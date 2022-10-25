package com.darkgames.server;

import Packets.AllScoresPacket;
import Packets.LobbyConnectionListPacket;
import Packets.PaddlesPacket;
import Packets.RemoveConnectionPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class Connection implements Runnable{
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private EventListener listener = new EventListener();
    public int id;
    public Connection(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {
                try {
                    Object data = in.readObject();
                    listener.received(data, this);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.out.println(id);
            RemoveConnectionPacket packet = new RemoveConnectionPacket();
            packet.id = id;
            LobbyConnectionListPacket packet2 = new LobbyConnectionListPacket();
            ConnectionHandler.connections.remove(id);
            ConnectionHandler.paddles.remove(id);
            ConnectionHandler.scoreTracker.remove(id);
            PaddlesPacket sendingPacket = new PaddlesPacket(ConnectionHandler.paddles);
            AllScoresPacket scoresPacket = new AllScoresPacket();
            for (HashMap.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                Integer key = entry.getKey();
                Connection value = entry.getValue();
                packet2.addUsers(value.id);
                scoresPacket.scoreTracker.put(value.id, ConnectionHandler.scoreTracker.get(value.id));
            }
            for (HashMap.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                Integer key = entry.getKey();
                Connection value = entry.getValue();
                if (value != null) {
                    value.sendObject(packet);
                    value.sendObject(packet2);
                    value.sendObject(sendingPacket);
                    value.sendObject(scoresPacket);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendObject(Object packet) {
        try {
            out.writeObject(packet);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
