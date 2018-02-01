package Source.GameProcess.Online.Server;

import Source.Core.Generator;
import Source.Data;
import java.io.IOException;
import java.net.Socket;

public class GameThread extends Thread{
    GameThread(Generator core, Socket[] playerSockets, int players){
        this.players=players;
        this.gen=core;
        this.playerSockets=playerSockets;
        start();
    }

    private Socket[] playerSockets;
    private Generator gen;
    private int players;

    @Override
    public void run() {
        PlayerThreads[] player = new PlayerThreads[this.players];
        Data core = gen.getData();
        for (int i=0;i<players;i++){
            try {
                player[i]=new PlayerThreads(playerSockets[i], core,i);
            } catch (IOException e) {
                try {
                    playerSockets[i].close();
                    synchronized (core.core.sync){
                        core.core.error=-2;
                    }
                } catch (IOException ignored) {
                }
            }
        }
        while (core.core.gameIsRunning){
            if(core.core.error!=0) {
                for (int i = 0; i < players; ++i) {
                    player[i].out.send("error "+ core.core.error);
                }
                core.core.gameIsRunning=false;
            }
            else{
                if(core.core.edited) {
                    String s;
                    synchronized (core.core.sync){
                        s=core.core.fieldToString();
                    }
                    for (int i = 0; i < players; ++i) {
                        player[i].out.send("field " + core.core.turn + " " + s);
                    }
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
