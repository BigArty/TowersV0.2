package Source.Core;

public class GenThread extends Thread {
    public GenThread(Source.Data core){
        data=core;
        try {
            core.semaphore.acquire();
        } catch (InterruptedException ignored) {
        }
    }


    private Source.Data data;
    public int res;

    @Override
    public void run() {
        res=data.core.init();
        data.semaphore.release();
    }
}
