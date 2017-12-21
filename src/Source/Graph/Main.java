package Source.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends Thread {
    public Main(Source.Data data) {
        this.data = data;
        start();
    }

    private Source.Data data;
    private JFrame f = new JFrame("TowersV0.2");

    @Override
    public void run() {
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height));
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Main Canvas---------
        CanvasMain cm = new CanvasMain(data);
        Listner L = new Listner(cm);
        f.addMouseListener(L);
        f.addMouseMotionListener(L);
        f.addMouseWheelListener(L);
        f.add(cm);
        cm.setVisible(true);
        f.setVisible(true);
        //---------------------
        while (data.core.gameIsRunning) {
            cm.repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
        }
    }
}

class Listner implements MouseListener, MouseWheelListener, MouseMotionListener {
    private double x = 0;
    private double y = 0;
    private boolean pressed = false;
    private CanvasMain canvasMain;

    Listner(CanvasMain cM) {
        canvasMain = cM;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            x = e.getX();
            y = e.getY();
            pressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            pressed = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (pressed) {
            x = x - e.getX();
            y = y - e.getY();
            canvasMain.dX += x * 1.0 / canvasMain.scale;
            canvasMain.dY += y * 1.0 / canvasMain.scale;
            x = e.getX();
            y = e.getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        canvasMain.scale *= Math.pow(1.05, -notches);
    }
}
