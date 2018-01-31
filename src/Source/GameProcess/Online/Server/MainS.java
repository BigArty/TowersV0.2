package Source.GameProcess.Online.Server;

public class MainS {
    public static void main(String[] args) {
        SortThread thr=new SortThread();
        new InitThread(thr);
        new StopThread(thr);
    }

}

