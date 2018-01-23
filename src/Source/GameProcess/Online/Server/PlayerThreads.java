package Source.GameProcess.Online.Server;

import Source.Data;

import java.io.*;
import java.net.Socket;

public class PlayerThreads{
    private int turn;
    PlayerThrInput in;
    PlayerThrOutput out;
    Data core;
    PlayerThreads(Socket socket, Data core, int turn) throws IOException {
        this.turn=turn;
        this.core=core;
        in=new PlayerThrInput(socket,this);
        out=new PlayerThrOutput(socket, this);
        out.send(turn+"");
    }
}

class PlayerThrInput extends Thread{
    private BufferedReader in;
    private PlayerThreads parent;
    private Socket s;
    PlayerThrInput(Socket socket, PlayerThreads playerThreads) throws IOException {
        s=socket;
        in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        parent=playerThreads;
        start();
    }

    @Override
    public void run() {
        while(parent.core.core.gameIsRunning){
            try {
                in.readLine();
            } catch (IOException e) {
                try {
                    s.close();
                    synchronized (parent.core.core.sync) {
                        parent.core.core.error = -2;
                    }
                } catch (IOException ignored) {
                }
            }
        }
    }
}

class PlayerThrOutput{
    PrintWriter out;
    public void send(String s){
        out.println(s);
    }
    public PlayerThrOutput(Socket socket, PlayerThreads playerThreads) throws IOException {
        out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                .getOutputStream())), true);
    }
}
