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

    private SortThread thr;

    @Override
    public void run() {
        try (ServerSocket s = new ServerSocket(PORT)) {
        while (thr.working) {
                Socket socket = s.accept();
                System.out.println("Suffering");
                thr.addSocket(socket);
                Thread.sleep(100);
        }
        } catch (IOException | InterruptedException ignored) {
        }
    }
}
