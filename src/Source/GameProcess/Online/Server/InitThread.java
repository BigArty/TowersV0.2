package Source.GameProcess.Online.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class InitThread extends Thread{
    private static final int PORT=8080;
    public static final int maxPlayers=9;
    InitThread(){
        start();
    }

    @Override
    public void run() {
        try (ServerSocket s = new ServerSocket(PORT)) {

        }
        catch (IOException ignored) {
        }

    }
}
