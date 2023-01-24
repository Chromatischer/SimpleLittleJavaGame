package main;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseClickManager implements MouseListener {
    public boolean mouseClicked;
    public int mouseX, mouseY;
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point ePoint = e.getPoint();
        mouseClicked = true;
        mouseX = ePoint.x;
        mouseY = ePoint.y;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point ePoint = e.getPoint();
        mouseClicked = false;
        mouseX = ePoint.x;
        mouseY = ePoint.y;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
