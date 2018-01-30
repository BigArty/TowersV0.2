package Source.Core;

import java.util.Date;
import java.util.LinkedList;

public class Main {
    public Main(int players) {
        numOfPlayers = players;
        field = new Cell[width][height];
        this.players = new Player[numOfPlayers];
        playerCells = new int[numOfPlayers];
        bestField = new int[numOfPlayers][startTower][2];
        bestDiff = height * width;
        epsInTerritory = height * width / 100;
        moves = new int[players];
    }

    public int error = 0;
    /*
    -1 - noName
    -2 - playerDisconnect;

    */
    private int[] moves;
    public int numOfPlayers;
    public int height = 300;
    public int width = 300;
    private int startTower = 9;
    private int maxGenerationTime = 1;
    private int maxInitRecursion = 20;
    private int epsInTerritory;
    public Cell[][] field;
    public boolean gameIsRunning = true;
    public Player[] players;
    public int turn = 0;
    public int[] playerCells;
    public final Object sync = new Object();
    public boolean generate = true;

    private int moveFunc(double x) {
        return (int) (300 * x * x * x * x * (1 - x) * (1 - x) + 2.5 * Math.exp(-6 * x) + 2);
    }

    private void moveCalc() {//balance, balance and again balance
        for (int i = 0; i < numOfPlayers; ++i) {
            moves[i] = moveFunc(1.0 * playerCells[i] / width / height);
        }
    }

    public void fieldFromString(String s) {
        clearField();
        clearTowers();
        String[] data = s.split(" ");
        for (int i = 0; i < data.length; i += 3) {
            putTower(Integer.parseInt(data[i + 3]), Integer.parseInt(data[i]), Integer.parseInt(data[i + 1]));
        }
        fieldCalc();
    }

    public String fieldToString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++i) {
                if (field[i][j].tower) {
                    s.append(i).append(" ").append(j).append(" ").append(field[i][j].player).append(" ");
                }
            }
        }
        return s.toString();
    }

    int fieldCalc() {
        for (int i = 0; i < numOfPlayers; ++i) {
            playerCells[i] = 0;
        }
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
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
                synchronized (sync) {
                    field[i][j].player = nearestPlr;
                }
                playerCells[nearestPlr]++;
            }
        }
        return 0;
    }

    void putTower(int player, int x, int y) {
        Tower tow = new Tower(player, x, y);
        players[player].towers.add(tow);
        synchronized (sync) {
            field[x][y].player = player;
            field[x][y].tower = true;
            field[x][y].tow = tow;
        }
    }

    int removeTower(int x, int y) {
        synchronized (sync) {
            players[field[x][y].player].towers.remove(field[x][y].tow);
            field[x][y].tow = null;
            field[x][y].tower = false;
        }
        return 0;
    }

    void clearTowers() {
        for (int i = 0; i < numOfPlayers; ++i) {
            players[i].towers.clear();
        }
    }

    void clearField() {
        synchronized (sync) {
            for (int i = 0; i < width; ++i) {
                for (int j = 0; j < height; ++j) {
                    field[i][j].tower = false;
                }
            }
        }
    }


    int startTowers1() {
        int startTimeSec = (int) (new Date().getTime() / 1000);
        int nowTime = startTimeSec;
        boolean correct = false;
        int count = 0;
        while (generate || (startTimeSec > nowTime - maxGenerationTime * maxInitRecursion)) {
            ++count;
            clearField();
            clearTowers();
            for (int i = 0; i < numOfPlayers * startTower; ++i) {
                int x, y;
                synchronized (sync) {
                    do {
                        x = (int) (Math.random() * width);
                        y = (int) (Math.random() * height);
                    } while (field[x][y].tower);
                }
                putTower(0, x, y);
            }
            int[] ready = new int[numOfPlayers];
            ready[0] = 1;
            int correctPl = 1;
            int x0 = 0, y0 = 0;
            while (correctPl < numOfPlayers) {
                int pl = (int) (Math.random() * numOfPlayers);
                while (ready[pl] == 1) {
                    pl++;
                    pl = pl % numOfPlayers;
                }
                int farX = x0, farY = y0;
                int dist2 = 0;
                LinkedList<Tower> tow = new LinkedList<>();
                for (Tower t : players[0].towers) {
                    if (dist2 < ((t.x - x0) * (t.x - x0) + (t.y - y0) * (t.y - y0))) {
                        dist2 = ((t.x - x0) * (t.x - x0) + (t.y - y0) * (t.y - y0));
                        farX = t.x;
                        farY = t.y;
                    }
                }
                x0 = farX;
                y0 = farY;
                synchronized (sync) {
                    tow.add(field[x0][y0].tow);
                    removeTower(field[x0][y0].tow.x, field[x0][y0].tow.y);
                }
                putTower(pl, x0, y0);
                for (Tower t : players[0].towers) {
                    int i = 0;
                    while ((i < tow.size()) && (((t.x - x0) * (t.x - x0) + (t.y - y0) * (t.y - y0)) > ((tow.get(i).x - x0) * (tow.get(i).x - x0) + (tow.get(i).y - y0) * (tow.get(i).y - y0)))) {
                        ++i;
                    }
                    tow.add(i, t);
                }

                for (int i = 0; i < startTower; ++i) {
                    removeTower(tow.get(i).x, tow.get(i).y);
                    putTower(pl, tow.get(i).x, tow.get(i).y);
                }

                correctPl++;
                ready[pl] = 1;
            }
            fieldCalc();
            int min = width * height, max = 0;
            for (int i = 0; i < numOfPlayers; ++i) {
                if (playerCells[i] > max) {
                    max = playerCells[i];
                }
                if (playerCells[i] < min) {
                    min = playerCells[i];
                }
            }
            if (bestDiff > max - min) {
                for (int i = 0; i < numOfPlayers; ++i) {
                    int j = 0;
                    for (Tower t : players[i].towers) {
                        bestField[i][j][0] = t.x;
                        bestField[i][j][1] = t.y;
                        ++j;
                    }
                }
                bestDiff = max - min;
            }
            nowTime = (int) (new Date().getTime() / 1000);
        }
        System.out.println(count + "");
        if (bestDiff < epsInTerritory) {
            correct = true;
        }
        if (!correct) {
            return -1;
        }
        return 0;
    }

    private int[][][] bestField;
    private int bestDiff;

    int startTowers(int recursion) {
        System.out.println("Try number " + recursion);
        /*putTower(0, 0, 0);
        putTower(1, width - 1, 0);
        putTower(2, width - 1, height - 1);
        putTower(3, 0, height - 1);*/
        boolean correct = false;
        int numOfRows = (int) Math.ceil(Math.sqrt(numOfPlayers));
        int[][] areas = new int[numOfRows][numOfRows];
        int[] playerX = new int[numOfPlayers];
        int[] playerY = new int[numOfPlayers];
        for (int i = 0; i < numOfPlayers; ++i) {
            int areaY;
            int areaX;
            do {
                areaX = (int) (Math.random() * numOfRows);
                areaY = (int) (Math.random() * numOfRows);
            } while (areas[areaX][areaY] == 1);
            areas[areaX][areaY] = 1;
            playerX[i] = width / numOfRows * areaX;
            playerY[i] = height / numOfRows * areaY;
        }
        int startTimeSec = (int) (new Date().getTime() / 1000);
        int nowTime = startTimeSec;
        while ((startTimeSec > nowTime - maxGenerationTime)) {
            clearField();
            clearTowers();
            for (int k = 0; k < numOfPlayers; ++k) {
                for (int i = 0; i < startTower; ++i) {
                    int x, y;
                    do {
                        x = (int) (Math.random() * width / numOfRows + playerX[k]);
                        y = (int) (Math.random() * height / numOfRows + playerY[k]);
                    } while (field[x][y].tower);
                    putTower(k, x, y);
                }
            }
            fieldCalc();
            int min = playerCells[0], diff = 0;
            for (int i = 0; i < numOfPlayers; ++i) {
                if (playerCells[i] < min) {
                    diff += min - playerCells[i];
                }
                if (playerCells[i] - min > diff) {
                    diff = playerCells[i] - min;
                }
            }
            if (diff < epsInTerritory) {
                correct = true;
            }
            if (diff < bestDiff) {
                for (int i = 0; i < numOfPlayers; ++i) {
                    int j = 0;
                    for (Tower t : players[i].towers) {
                        bestField[i][j][0] = t.x;
                        bestField[i][j][1] = t.y;
                        ++j;
                    }
                }
                bestDiff = diff;
            }
            nowTime = (int) (new Date().getTime() / 1000);
        }
        if (!correct) {
            if (recursion + 1 > maxInitRecursion) {
                return -1;
            }
            return startTowers(recursion + 1);
        }
        return 0;
    }

    private int loadBestField() {
        clearField();
        clearTowers();
        System.out.println("Best diff " + bestDiff * 1.0 / width / height);
        for (int i = 0; i < numOfPlayers; ++i) {
            for (int j = 0; j < startTower; ++j) {
                putTower(i, bestField[i][j][0], bestField[i][j][1]);
            }
        }
        fieldCalc();
        for (int i = 0; i < numOfPlayers; ++i) {
            System.out.print(playerCells[i] + " ");
        }
        moveCalc();

        return bestDiff;
    }

    public int move(int x, int y, int player) {
        if (turn == player) {
            if (field[x][y].tower) {
                if (field[x][y].tow.player != player) {
                    removeTower(x, y);
                    fieldCalc();
                    moves[player]--;
                    endOfTurn(player);
                    return 0;
                } else {
                    return -1;
                }
            } else {
                if (field[x][y].player == player) {
                    putTower(player, x, y);
                    fieldCalc();
                    moves[player]--;
                    endOfTurn(player);
                    return 0;
                } else {
                    return -1;
                }
            }
        } else {
            return -1;
        }
    }

    private void endOfTurn(int player) {
        if (moves[player] <= 0) {
            turn++;
            if (turn >= numOfPlayers) {
                turn = 0;
            }
            if (player == numOfPlayers - 1) {
                moveCalc();
            }
        }
    }

    public int init() {
        synchronized (sync) {
            for (int i = 0; i < width; ++i) {
                for (int j = 0; j < height; ++j) {
                    field[i][j] = new Cell();
                }
            }
        }
        for (int i = 0; i < numOfPlayers; ++i) {
            players[i] = new Player(i);
        }
        int err = startTowers1();
        int diff = loadBestField();
        if (err != 0) {
            System.out.println("Can't create fair field");
            System.out.println();
            //System.exit(-1);
        }
        return diff;
    }


}
