package Source.Core;

public class Main {
    public int width = 300;
    public int height = 300;
    public int numOfPlayers = 4;
    public Cell[][] field = new Cell[width][height];
    public boolean gameIsRunning = true;
    public Player[] players = new Player[numOfPlayers];
    public int turn = 0;
    public int[] playerCells=new int[numOfPlayers];

    int fieldCalc() {
        for(int i=0;i<numOfPlayers;++i){
            playerCells[i]=0;
        }
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                int nearestPlr=-1;
                int distance = (width+height)*(width+height);
                for (int k = 0; k < numOfPlayers; ++k) {
                    for (Tower t : players[k].towers) {
                        if (distance > ((t.x - i) * (t.x - i) + (t.y - j) * (t.y - j))) {
                            distance = ((t.x - i) * (t.x - i) + (t.y - j) * (t.y - j));
                            nearestPlr=t.player;
                        }
                    }
                }
                field[i][j].player=nearestPlr;
                playerCells[nearestPlr]++;
            }
        }
        return 0;
    }

    int init() {
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                field[i][j] = new Cell();
            }
        }
        for (int i = 0; i < numOfPlayers; ++i) {
            players[i] = new Player(i);
        }
        return 0;
    }


}
