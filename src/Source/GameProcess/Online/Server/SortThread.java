package Source.GameProcess.Online.Server;

import Source.Core.Generator;
import Source.Data;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SortThread extends Thread {
    int maxPlay = 10;

    private boolean supportedPlrNmb(int players){
        switch (players) {
            case 2:
                return true;
            case 3:
                return true;
            case 4:
                return true;
            case 5:
                return true;
            case 9:
                return true;
            default:
                return false;
        }
    }

    SortThread() {
        ready = new int[maxPlay];
        generator = new Generator[maxPlay];
        for (int i = 0; i < maxPlay; ++i) {
            if(supportedPlrNmb(i)) {
                startGenerator(i);
            }
        }
        for (int i = 0; i < sockets.length; ++i) {
            sockets[i] = new Socket[i];
        }
        start();
    }

    boolean working = true;

    private int ready[];
    private Generator[] generator;
    private Socket[][] sockets = new Socket[maxPlay][];
    private List<Socket> queue = Collections.synchronizedList(new LinkedList<>());

    private void startGenerator(int players) {
        generator[players] = new Generator(players, true);
    }

    public void addSocket(Socket s) {
        queue.add(s);
    }

    private int connect(Socket s, int players) {
        if(!supportedPlrNmb(players)){
            return -1;
        }
        sockets[players][ready[players]] = s;
        ready[players]++;
        if (ready[players] >= players - 1) {
            generator[players].setGenerate(false);
            new GameThread(generator[players].getData(), sockets[players], players);
            ready[players] = 0;
            sockets[players]=new Socket[players];
            generator[players]=new Generator(players,true);
        }
        return 0;
    }

    @Override
    public void run() {
        while (working) {
            while (!queue.isEmpty()) {
                Socket socket = queue.remove(0);
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    int players = Integer.parseInt(in.readLine());
                    int err = connect(socket, players);
                    if (err != 0) {
                        new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true).print(err);
                        socket.close();
                    }
                } catch (IOException e) {
                    try {
                        socket.close();
                    } catch (IOException ignored) {
                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
