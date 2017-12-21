package Source.Core;

import java.util.Date;

public class Main {
    public int numOfPlayers = 4;
    public int height = 300;
    public int width = 300;
    private int startTower = 6;
    private int maxGenerationTime = 3;
    private int maxInitRecursion = 20;
    private int epsInTerritory = height*width/100;
    public Cell[][] field = new Cell[width][height];
    public boolean gameIsRunning = true;
    public Player[] players = new Player[numOfPlayers];
    public int turn = 0;
    public int[] playerCells = new int[numOfPlayers];

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
                field[i][j].player = nearestPlr;
                playerCells[nearestPlr]++;
            }
        }
        return 0;
    }

    void putTower(int player, int x, int y) {
        Tower tow=new Tower(player, x, y);
        players[player].towers.add(tow);
        field[x][y].player = player;
        field[x][y].tower = true;
        field[x][y].tow=tow;
    }

    void clerTowers() {
        for (int i = 0; i < numOfPlayers; ++i) {
            players[i].towers.clear();
        }
    }

    void clearField() {
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                field[i][j].tower = false;
            }
        }
    }

    private int[][][] bestField=new int[numOfPlayers][startTower][2];
    private int bestDiff=height*width;

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
        int startTimeSec = (int) (new Date().getTime() / 1000 );
        int nowTime = startTimeSec;
        while ((startTimeSec > nowTime-maxGenerationTime)) {
            clearField();
            clerTowers();
            for (int k = 0; k < numOfPlayers; ++k) {
                for (int i = 0; i < startTower; ++i) {
                    int x,y;
                    do {
                        x = (int) (Math.random() * width / numOfRows + playerX[k]);
                        y = (int) (Math.random() * height / numOfRows + playerY[k]);
                    }while (field[x][y].tower);
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
            else{
                if(diff<bestDiff){
                    for (int i=0;i<numOfPlayers;++i){
                        int j=0;
                        for (Tower t:players[i].towers){
                            bestField[i][j][0]=t.x;
                            bestField[i][j][1]=t.y;
                            ++j;
                        }
                    }
                    bestDiff=diff;
                }
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

    private void loadBestField(){
        clearField();
        clerTowers();
        System.out.println("Best diff "+ bestDiff*1.0/width/height);
        for (int i=0;i<numOfPlayers;++i){
            for (int j=0;j<startTower;++j){
                putTower(i,bestField[i][j][0],bestField[i][j][1]);
            }
        }
        fieldCalc();
    }

    public void move(int x, int y, int player) {
        if (turn == player) {

        }
    }

    public int init() {
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                field[i][j] = new Cell();
            }
        }
        for (int i = 0; i < numOfPlayers; ++i) {
            players[i] = new Player(i);
        }
        int err = startTowers(0);
        loadBestField();
        if (err != 0) {
            System.out.println("Can't create fair field");
            for(int i=0;i<numOfPlayers;++i){
                System.out.print(playerCells[i]+" ");
            }
            System.out.println();
            //System.exit(-1);
        }
        fieldCalc();
        return 0;
    }


}
