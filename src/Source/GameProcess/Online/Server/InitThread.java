package Source.GameProcess.Online.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InitThread extends Thread {
    private static final int PORT = 8080;

    InitThread(SortThread thr) {
        this.thr = thr;
        new StopThread(thr);
        start();
    }

    SortThread thr;

    @Override
    public void run() {
        while (thr.working) {
            try (ServerSocket s = new ServerSocket(PORT)) {
                Socket socket = s.accept();
                thr.addSocket(socket);
            } catch (IOException ignored) {
            }
        }
    }
}
