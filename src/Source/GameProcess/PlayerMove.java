package Source.GameProcess;

import Source.Data;

import java.util.concurrent.SynchronousQueue;

public class PlayerMove extends Thread {
    public int turn;
    private Data data;
    private SynchronousQueue<PlayerEvent> queue;
    PlayerMove(Data d){
        data=d;
        queue=new SynchronousQueue<>();
        start();
    }

    public void addMove(int x,int y, int player){
        queue.add(new PlayerEvent(x,y,player));
    }

    @Override
    public void run() {
        while (data.core.gameIsRunning){
            if(!queue.isEmpty()){

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