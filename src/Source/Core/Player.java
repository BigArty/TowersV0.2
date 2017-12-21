package Source.Core;

import java.util.HashSet;
import java.util.TreeSet;

public class Player {
    int id;
    HashSet<Tower> towers;

    public Player(int i) {
        id=i;
        towers=new HashSet<>();
    }
}
