package Source.Core;

import Source.Data;

import java.util.concurrent.Semaphore;

public class Generator extends Thread{

    public Generator(int players, boolean generate){
        gen=generate;
        this.players=players;
        start();
    }

    private boolean gen;
    private int players;

    @Override
    public void run() {
        data=startNewGame(players,gen);
    }

    static final int cores = 7;
    private Source.Core.GenThread[] genThreads;
    public void setGenerate(boolean generate){
        for(int i=0;i<cores;++i){
            genThreads[i].setGenerate(generate);
        }
    }

    private boolean ready=false;
    private Data data;

    public Data getData() {
        while (!ready) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
        return data;
    }

    public Data startNewGame(int players, boolean generate) {
        genThreads = new GenThread[cores];
        Data[] data = new Data[cores];
        Semaphore semaphore = new Semaphore(cores, true);
        for (int i = 0; i < cores; ++i) {
            data[i] = new Data();
            data[i].semaphore = semaphore;
            data[i].core = new Source.Core.Main(players);
            data[i].core.generate = generate;
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
        ready=true;
        return data[bestCore];
    }
}
