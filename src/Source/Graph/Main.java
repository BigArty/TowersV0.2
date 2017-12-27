package Source.Graph;

import Source.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends Thread {
    public Main() {
        sync =new Object();
        ready=false;
        start();
    }

    public void setData(Data data) {
        synchronized (sync){
            this.data = data;
            ready=true;
        }
    }

    private final Object sync;

    private Source.Data data;
    private boolean ready;
    private JFrame f = new JFrame("TowersV0.2");

    @Override
    public void run() {
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height));
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Loading-------------
        JLabel loading = new JLabel("Loading");
        loading.setVerticalAlignment(JLabel.CENTER);
        loading.setHorizontalAlignment(JLabel.CENTER);
        f.add(loading);
        f.repaint();
        f.setVisible(true);
        f.repaint();

        synchronized (sync) {
            while (!ready) {
                try {
                    sync.wait(100);
                } catch (InterruptedException ignored) {
                }
            }
        }

        f.remove(loading);
        //Main Canvas---------
        CanvasMain cm = new CanvasMain(data);
        Listener L = new Listener(cm);
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

class Listener implements MouseListener, MouseWheelListener, MouseMotionListener {
    private double x = 0;
    private double y = 0;
    private boolean pressed = false;
    private CanvasMain canvasMain;

    Listener(CanvasMain cM) {
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
