package org.bitwave.ve3.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(3000);
             Socket socket = server.accept();
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            System.out.println("Server started. Waiting for Client.");
            String line;
            while (!(line = in.readUTF()).equals("End")) {
                System.out.println("Client: " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
