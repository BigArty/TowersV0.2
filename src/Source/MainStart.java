package Source;


import Source.Core.GenThread;

import java.util.concurrent.Semaphore;

public class MainStart {
    public static void main(String[] args) {
        int n=5;
        int players=9;
        Source.Core.GenThread[] genThreads=new GenThread[n];
        Data[] data=new Data[n];
        Semaphore semaphore=new Semaphore(n,true);
        for (int i=0;i<n;++i){
            data[i]=new Data();
            data[i].semaphore=semaphore;
            data[i].core=new Source.Core.Main(players);
            genThreads[i]=new GenThread(data[i]);
        }
        for (int i=0;i<n;++i){
            genThreads[i].start();
        }
        try {
            semaphore.acquire(n);
        } catch (InterruptedException ignored) {
        }
        semaphore.release();
        int bestRes=1;
        int bestCore=0;
        for (int i=0;i<n;++i){
            System.out.println("Core "+ i+" res "+ genThreads[i].res);
            if(genThreads[i].res<=bestRes){
                bestRes=genThreads[i].res;
                bestCore=i;
            }
        }
        Source.Graph.Main mainWindow = new Source.Graph.Main(data[bestCore]);
    }
}
