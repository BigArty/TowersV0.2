package Source;

public class MainStart {
    public static void main(String[] args) {
        Data data=new Data();
        data.core=new Source.Core.Main();
        data.core.init();
        Source.Graph.Main main = new Source.Graph.Main(data);
    }
}
