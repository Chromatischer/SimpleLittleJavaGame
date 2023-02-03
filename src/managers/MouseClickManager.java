package managers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseClickManager implements MouseListener {
    public boolean LeftMouseClicked, RightMouseClicked;
    public int mouseX, mouseY;
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point ePoint = e.getPoint();
        if (SwingUtilities.isLeftMouseButton(e)){
            LeftMouseClicked = true;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            RightMouseClicked = true;
        }
        mouseX = ePoint.x;
        mouseY = ePoint.y;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point ePoint = e.getPoint();
        if (SwingUtilities.isLeftMouseButton(e)){
            LeftMouseClicked = false;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            RightMouseClicked = false;
        }
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
