package Source;

import Source.Core.Generator;
import Source.GameProcess.ConsoleGame;

public class MainStart {
    public static void main(String[] args) {
        int players = 2;
        Source.Graph.Main mainWindow = new Source.Graph.Main();
        Generator gen=new Generator(players,false);
        Data d = gen.getData();
        mainWindow.setData(d);
        new ConsoleGame(d);
    }
}
