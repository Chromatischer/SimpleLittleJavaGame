package gui;

import managers.ImageManager;

import java.awt.image.BufferedImage;

public class DialogueField{
    BufferedImage topLeft, topRight, bottomLeft, bottomRight, middleLeft, middleRight, middleTop, middleBottom, middle;

    public DialogueField(){
        String maplocation = "/res/ui/dialogue_map.png";
        topLeft         = ImageManager.getTile(0,0,16,16,48,48, maplocation);
        middleTop       = ImageManager.getTile(16,0,16,16,48,48, maplocation);
        topRight        = ImageManager.getTile(32,0,16,16,48,48, maplocation);

        middleLeft      = ImageManager.getTile(0,16,16,16,48,48, maplocation);
        middle          = ImageManager.getTile(16,16,16,16,48,48, maplocation);
        middleRight     = ImageManager.getTile(32,16,16,16,48,48, maplocation);

        bottomLeft      = ImageManager.getTile(0,32,16,16,48,48, maplocation);
        middleBottom    = ImageManager.getTile(16,32,16,16,48,48, maplocation);
        bottomRight     = ImageManager.getTile(32,32,16,16,48,48, maplocation);
    }
}
