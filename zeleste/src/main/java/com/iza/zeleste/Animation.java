package com.iza.zeleste;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Animation {
    private Image spriteSheet;
    private int frameCount;
    private int row;
    private double frameDuration = 0.1;

    public Animation(Image spriteSheet, int row, int frameCount){
        this.spriteSheet = spriteSheet;
        this.row = row;
        this.frameCount = frameCount;
    }

    public void draw(GraphicsContext gc, double x, double y, double timer, boolean flipped){
        int frameIndex = (int) (timer/frameDuration) % frameCount;
        int spriteSize = 32;

        double sx = frameIndex * spriteSize;
        double sy = row * spriteSize;

        if(flipped){
            gc.drawImage(spriteSheet, sx, sy, spriteSize, spriteSize, x + spriteSize, y, -spriteSize, spriteSize);
        }else {
            gc.drawImage(spriteSheet, sx, sy, spriteSize, spriteSize, x, y, spriteSize, spriteSize);
        }
    }







    public Image getSpriteSheet() {
        return spriteSheet;
    }



}
