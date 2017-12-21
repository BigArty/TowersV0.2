package Source.Core;

public class Main {
    public int height = 300;
    public int width = 300;
    public int numOfPlayers = 4;
    public Cell[][] field = new Cell[height][width];
    public boolean gameIsRunning = true;
    public Player[] players = new Player[numOfPlayers];
    public int turn = 0;
    public int[] playerCells = new int[numOfPlayers];

    int fieldCalc() {
        for (int i = 0; i < numOfPlayers; ++i) {
            playerCells[i] = 0;
        }
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int nearestPlr = 0;
                int distance = (height + width) * (height + width);
                for (int k = 0; k < numOfPlayers; ++k) {
                    for (Tower t : players[k].towers) {
                        if (distance > ((t.x - i) * (t.x - i) + (t.y - j) * (t.y - j))) {
                            distance = ((t.x - i) * (t.x - i) + (t.y - j) * (t.y - j));
                            nearestPlr = t.player;
                        }
                    }
                }
                field[i][j].player = nearestPlr;
                playerCells[nearestPlr]++;
            }
        }
        return 0;
    }

    void putTower(int player, int x, int y) {
        players[player].towers.add(new Tower(player, x, y));
        field[x][y].player = player;
        field[x][y].tower = true;
    }

    int startTowers() {
        putTower(0, 0, 0);
        putTower(1, width - 1, 0);
        putTower(2, width - 1, height - 1);
        putTower(3, 0, height - 1);
        return 0;
    }

    public int init() {
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                field[i][j] = new Cell();
            }
        }
        for (int i = 0; i < numOfPlayers; ++i) {
            players[i] = new Player(i);
        }
        startTowers();
        fieldCalc();
        return 0;
    }


}
