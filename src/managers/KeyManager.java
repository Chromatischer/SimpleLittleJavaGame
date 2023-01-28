package managers;
import java.awt.event.*;
public class KeyManager implements KeyListener{
    public boolean upPressed, downPressed, leftPressed, rightPressed, inventoryPressed;

    @Override
    public void keyTyped(KeyEvent e) {} //not used


    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode(); //returns keynumber
        //TODO: maybe add something like a more advanced key input listener in here!
        if (code == KeyEvent.VK_W){
            upPressed = true;
        }
        if (code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S){
            downPressed = true;
        }
        if (code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if (code == KeyEvent.VK_E){
            inventoryPressed =! inventoryPressed;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode(); //returns keynumber
        if (code == KeyEvent.VK_W){
            upPressed = false;
        }
        if (code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S){
            downPressed = false;
        }
        if (code == KeyEvent.VK_D){
            rightPressed = false;
        }
    }
}

