package Source;

import Source.Core.Generator;

public class MainStart {
    public static void main(String[] args) {
        int players = 9;
        Source.Graph.Main mainWindow = new Source.Graph.Main();
        Data d = new Generator().startNewGame(players, false);
        mainWindow.setData(d);
    }
}
