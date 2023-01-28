package main.object;

import main.SuperObject;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OBJChest extends SuperObject {
    public OBJChest(){
        name = "Chest";
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/object/chest.png")));
        } catch (IOException e){
            e.printStackTrace();
        }
        //solidArea = new Rectangle(0, 0, 48, 48); can be used if different hitbox size is needed!
        collision = true;
    }
}
