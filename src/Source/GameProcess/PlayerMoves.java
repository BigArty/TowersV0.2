package Source.GameProcess;

import Source.Data;

import java.util.concurrent.SynchronousQueue;

public class PlayerMoves extends Thread {
    public int turn;
    private Data data;
    private SynchronousQueue<PlayerEvent> queue;
    private final Object sync;
    PlayerMoves(Data d){
        data=d;
        queue=new SynchronousQueue<>();
        start();
        sync=new Object();
    }

    public void addMove(int x,int y, int player){
        queue.add(new PlayerEvent(x,y,player));
    }

    @Override
    public void run() {
        while (data.core.gameIsRunning){
            while(!queue.isEmpty()){
            }
        }
    }
}

class PlayerEvent{
    int x,y,player;
    PlayerEvent(int x,int y,int player){
        this.x=x;
        this.y=y;
        this.player=player;
    }
}