package Source;


import Source.Core.GenThread;

import java.util.concurrent.Semaphore;

public class MainStart {
    static final int cores = 7;

    public static Data startNewGame(int players) {
        Source.Graph.Main mainWindow = new Source.Graph.Main();
        Source.Core.GenThread[] genThreads = new GenThread[cores];
        Data[] data = new Data[cores];
        Semaphore semaphore = new Semaphore(cores, true);
        for (int i = 0; i < cores; ++i) {
            data[i] = new Data();
            data[i].semaphore = semaphore;
            data[i].core = new Source.Core.Main(players);
            genThreads[i] = new GenThread(data[i]);
        }
        for (int i = 0; i < cores; ++i) {
            genThreads[i].start();
        }
        try {
            semaphore.acquire(cores);
        } catch (InterruptedException ignored) {
        }
        semaphore.release();
        int bestRes = 1;
        int bestCore = 0;
        for (int i = 0; i < cores; ++i) {
            System.out.println("Core " + i + " res " + genThreads[i].res);
            if (genThreads[i].res <= bestRes) {
                bestRes = genThreads[i].res;
                bestCore = i;
            }
        }
        mainWindow.setData(data[bestCore]);
        return data[bestCore];
    }

    public static void main(String[] args) {
        int players = 9;
        startNewGame(players);
    }
}
