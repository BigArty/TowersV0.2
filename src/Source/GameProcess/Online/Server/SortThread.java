package Source.GameProcess.Online.Server;

import Source.Core.Generator;
import Source.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SortThread extends Thread {
    SortThread() {
        for (int i = 0; i < ready.length; ++i) {
            ready[i] = i;
        }
        start();
    }

    boolean working = true;

    int ready[] = new int[10];
    Source.Data core[] = new Data[10];
    private List<Socket> queue = Collections.synchronizedList(new LinkedList<>());

    public void addSocket(Socket s) {
        queue.add(s);
    }

    private int connect(Socket s, int players) {
        switch (players) {
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 9:
                break;
            default:
                return -1;
        }
        if (ready[players] >= players) {
            Generator generator = new Generator();
            core[players] = generator.startNewGame(players, true);
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
                    connect(socket, players);
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
