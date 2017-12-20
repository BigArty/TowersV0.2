package Source.Graph;

import javax.swing.*;
import java.awt.*;

public class CanvasMain extends JComponent {
    double dX = 0;
    double dY = 0;
    double scale = 1;
    Color[] colors;
    int shiftX=100, shiftY=100;
    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    double x0(int x) {
        return width / 2.0 + (shiftX + x - dX - width / 2) * scale;
    }

    double y0(int y) {
        return height / 2.0 + (shiftY + y - dY - height / 2) * scale;
    }

    public void paintComponent(Graphics a) {
        Graphics2D g = (Graphics2D) a;
        g.drawRect((int) x0(0), (int) y0(0),(int) (10*scale),(int) (10*scale));
        g.drawRect((int) x0(100), (int) y0(100),(int) (100*scale),(int) (100*scale));
    }
}
