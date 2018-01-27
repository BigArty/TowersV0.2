package Source.GameProcess.Online.Server;

import Source.Data;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.JSONFunctions;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class GameThread extends Thread{
    GameThread(Data core, Socket[] playerSockets, int players){
        this.players=players;
        this.core=core;
        player = new PlayerThreads[this.players];
        for (int i=0;i<players;i++){
            try {
                player[i]=new PlayerThreads(playerSockets[i],core,i);
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
        start();
    }
    int players;
    Data core;
    PlayerThreads[] player;

    @Override
    public void run() {
        while (core.core.gameIsRunning){
            for(int i=0;i<players;++i){
                player[i].out.send("field "+core.core.fieldToString());
            }
            if(core.core.error!=0) {
                for (int i = 0; i < players; ++i) {
                    player[i].out.send("error "+core.core.error);
                }
                core.core.gameIsRunning=false;
            }
        }
    }
}
