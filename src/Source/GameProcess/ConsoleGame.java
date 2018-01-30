package Source.GameProcess;

import Source.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleGame extends Thread {
    public ConsoleGame(Data core) {
        this.core = core;
        start();
    }

    Data core;

    @Override
    public void run() {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        while (core.core.gameIsRunning) {
            try {
                String[] pMsg = r.readLine().split(" ");
                if (pMsg[0].equals("move")) {
                    synchronized (core.core.sync) {
                        int resp=core.core.move(Integer.parseInt(pMsg[2]), Integer.parseInt(pMsg[3]), Integer.parseInt(pMsg[1]));
                        System.out.println(resp);
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }
}
