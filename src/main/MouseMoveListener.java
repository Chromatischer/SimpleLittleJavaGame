package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMoveListener implements MouseMotionListener {
    public int locationX;
    public int locationY;
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        locationX = e.getX();
        locationY = e.getY();
    }
}
