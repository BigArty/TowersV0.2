package Source.Core;

public class GenThread extends Thread {
    public GenThread(Source.Data core) {
        data = core;
        try {
            core.semaphore.acquire();
        } catch (InterruptedException ignored) {
        }
    }


    private Source.Data data;

    public void setGenerate(boolean generate) {
        data.core.generate = generate;
    }

    public int res;

    @Override
    public void run() {
        res = data.core.init();
        data.semaphore.release();
    }
}
