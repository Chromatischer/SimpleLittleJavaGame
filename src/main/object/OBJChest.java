package main.object;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class OBJChest extends SuperObject{
    public OBJChest(){
        name = "Chest";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/object/chest.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
        //solidArea = new Rectangle(0, 0, 48, 48); can be used if different hitbox size is needed!
        collision = true;
    }
}
