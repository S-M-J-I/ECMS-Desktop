package project.controllers;

import project.views.ViewFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class BaseController extends Thread{
    private ViewFactory viewFactory;
    private String fxmlName;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public BaseController(ViewFactory viewFactory, String fxmlName) {
        this.viewFactory = viewFactory;
        this.fxmlName = fxmlName;
        connectSocket();
    }

    public void connectSocket() {
        try {
            socket = new Socket("localhost", 5000);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Shutting down");
            System.exit(0);
        }
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public String getFxmlName() {
        return fxmlName;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }
}
