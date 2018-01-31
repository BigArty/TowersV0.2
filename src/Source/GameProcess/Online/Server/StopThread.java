package Source.GameProcess.Online.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StopThread extends Thread {
    StopThread(SortThread thr){
        this.thread=thr;
        start();
    }
    SortThread thread;

    @Override
    public void run() {
        BufferedReader r=new BufferedReader(new InputStreamReader(System.in));
        while(true){
            try {
                String s=r.readLine();
                if(s.equals("stop")||s.equals("Stop")){
                    thread.working=false;
                    Thread.sleep(2000);
                    System.exit(0);
                }
            } catch (IOException | InterruptedException ignored) {
            }
        }
    }
}
