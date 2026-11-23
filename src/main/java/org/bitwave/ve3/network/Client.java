package org.bitwave.ve3.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 3000);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to Server");
            String line;
            while (!(line = reader.readLine()).equals("End")) {
                out.writeUTF(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
