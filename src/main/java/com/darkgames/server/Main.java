package com.darkgames.server;

import javax.swing.*;
import java.awt.*;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Main extends JFrame {
    private static Main serverContainer;
    public JPanel MainMenu;
    private JLabel ErrorText = new JLabel("");
    private JLabel IPText = new JLabel();
    private JLabel PortText = new JLabel();
    private JLabel PortInputText = new JLabel("Enter Port:");
    private JTextField PortInput = new JTextField();
    public static String ip;
    protected int Port;
    public static Server server;
    JButton StartBtn;
    JButton StopBtn;
    public Main() {
        initUI();
    }

    private void initUI() {
        setTitle("Breakout Server");

        MainMenu = new JPanel();
        MainMenu.setBackground(Color.ORANGE);
        MainMenu.setPreferredSize(new Dimension(500, 300));
        MainMenu.setVisible(true);

        //Define the Layout
        GridBagLayout layout = new GridBagLayout();
        MainMenu.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();

        //Add ErrorText
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        ErrorText.setVisible(false);
        MainMenu.add(ErrorText, gbc);

        //Add IPText
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        IPText.setVisible(true);
        MainMenu.add(IPText, gbc);

        //Add PortText
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        PortText.setVisible(true);
        MainMenu.add(PortText, gbc);

        //Add PortInputText
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        PortInputText.setVisible(true);
        MainMenu.add(PortInputText, gbc);

        //Add PortInput
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 4;
        PortInput.setVisible(true);
        MainMenu.add(PortInput, gbc);

        //DEFINE THE Start BUTTON
        StartBtn = new JButton("Start");
        StartBtn.addActionListener(e -> startServer());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 5;
        StartBtn.setVisible(true);
        MainMenu.add(StartBtn, gbc);

        //DEFINE THE Stop Button
        StopBtn = new JButton("Stop");
        StopBtn.addActionListener(e -> stopServer());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 6;
        StopBtn.setVisible(false);
        MainMenu.add(StopBtn, gbc);

        add(MainMenu);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
    }

    private void startServer() {
        if(CheckInput(PortInput.getText())) {
            Port = Integer.parseInt(PortInput.getText());
            ErrorText.setText("");
            IPText.setText("Current IP: " + Main.ip);
            PortText.setText("Current Port: " + Port);
            server = new Server(Port, Main.ip);
            server.start();
            StartBtn.setVisible(false);
            StopBtn.setVisible(true);
        } else {
            ErrorText.setText("Port Number is Incorrect, Enter an Integer Between 3000 and 65535");
        }
    }

    private void stopServer() {
        server.shutdown();
        StartBtn.setVisible(true);
        StopBtn.setVisible(false);
    }

    protected boolean CheckInput(String Input) {
        if(!Input.isEmpty()) {
            if (Input.matches("^[0-9]*$")) {
                if(Integer.parseInt(Input) > 3000 && Integer.parseInt(Input) < 65535) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    // *EDIT*
                    if (addr instanceof Inet6Address) continue;

                    ip = addr.getHostAddress();
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        EventQueue.invokeLater(() -> {
            serverContainer = new Main();
            serverContainer.setVisible(true);
        });
    }
}
