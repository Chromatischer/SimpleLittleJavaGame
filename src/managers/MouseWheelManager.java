package managers;

import utilities.Logger;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseWheelManager implements MouseWheelListener {
    public int currentCount, lastCount;
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        lastCount = currentCount;
        currentCount += e.getWheelRotation();
    }
}
