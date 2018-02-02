package Source.Graph;

import Source.Core.Cell;
import Source.Core.Main;
import Source.Data;

import javax.swing.*;
import java.awt.*;

public class CanvasMain extends JComponent {
    double dX = 0;
    double dY = 0;
    double scale = 1;
    int pixForCell = 2;
    int pixForTower = 8;
    Color[] colors = new Color[10];
    int shiftX = 100, shiftY = 100;
    private Source.Data data;
    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    void colorSet() {
        colors[0] = Color.BLUE;
        colors[1] = Color.GRAY;
        colors[2] = Color.green;
        colors[3] = Color.red;
        colors[4] = Color.pink;
        colors[5] = Color.CYAN;
        colors[6] = Color.ORANGE;
        colors[7] = Color.magenta;
        colors[8] = Color.DARK_GRAY;
        colors[9] = Color.BLACK;
    }

    public CanvasMain(Data data) {
        this.data = data;
        colorSet();
    }

    int x0(int x) {
        return (int) (width / 2.0 + (shiftX + x - dX - width / 2) * scale);
    }

    int y0(int y) {
        return (int) (height / 2.0 + (shiftY + y - dY - height / 2) * scale);
    }

    public void paintComponent(Graphics g) {
        //synchronized (data.core.sync) {
            for (int i = 0; i < data.core.width; ++i) {
                for (int j = 0; j < data.core.height; ++j) {
                    g.setColor(colors[data.core.field[i][j].player]);
                    g.fillRect(x0(i * pixForCell), y0(j * pixForCell), (int) (pixForCell * scale + 1), (int) (pixForCell * scale + 1));
                }
            }
            for (int i = 0; i < data.core.width; ++i) {
                for (int j = 0; j < data.core.height; ++j) {
                    if (data.core.field[i][j].tower) {
                        g.setColor(Color.black);
                        g.fillRect((int) (x0(i * pixForCell) - (pixForTower - pixForCell) * scale / 2), (int) (y0(j * pixForCell) - (pixForTower - pixForCell) * scale / 2), (int) (pixForTower * scale), (int) (pixForTower * scale));
                    }
                }
            }
            g.drawString(String.valueOf(data.core.turn),10,10);
        //}
    }
}
