package main.object;

import main.SuperObject;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OBJHealthPotion extends SuperObject {
    public OBJHealthPotion(){
        name = "Health_potion";
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/object/health_potion.png")));
        } catch (IOException e){
            e.printStackTrace();
        }
        collision = true;
    }
}