package Source.GameProcess.Online.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InitThread extends Thread{
    private static final int PORT=8080;
    public static final int maxPlayers=9;
    InitThread(SortThread thr){
        this.thr=thr;
        start();
    }
    SortThread thr;
    @Override
    public void run() {
        try (ServerSocket s = new ServerSocket(PORT)) {
            Socket socket=s.accept();
            thr.addSocket(socket);
        }
        catch (IOException ignored) {
        }

    }
}
