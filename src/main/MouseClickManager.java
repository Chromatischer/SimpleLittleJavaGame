package main;

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
        mouseClicked = true;
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseClicked = false;
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    public int getMouseX(MouseEvent e){
        return e.getX();
    }
    public int getMouseY(MouseEvent e){
        return  e.getY();
    }
}
