package com.darkgames.server;

import Packets.AddConnectionPacket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    private int port;
    private int id = 0;
    private ServerSocket serverSocket;
    private boolean running = false;

    public Server(int port, String host) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port , 3, InetAddress.getByName(host));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        running = true;
        System.out.println("Server started on port: " + port);
        while(running) {
            try {
                Socket socket = serverSocket.accept();
                if(ConnectionHandler.connections.size() < 3) {
                    System.out.println("Opening the Socket");
                    initSocket(socket);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        shutdown();
    }
    private void initSocket(Socket socket) {
        Connection connection = new Connection(socket, id);
        ConnectionHandler.connections.put(id,connection);
        AddConnectionPacket packet = new AddConnectionPacket(id);
        connection.sendObject(packet);
        System.out.println(ConnectionHandler.connections.get(id));
        new Thread(connection).start();
        id++;
    }

    public void shutdown() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
