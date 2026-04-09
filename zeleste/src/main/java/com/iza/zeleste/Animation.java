package com.iza.zeleste;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public class Animation {
    private List<Image> frames;
    private double frameDuration = 0.1;

    public Animation(List<Image> frames, double frameDuration){
        this.frames = frames;
        this.frameDuration = frameDuration;
    }

    public void draw(GraphicsContext gc, double x, double y, double timer, boolean flipped){
        if(frames.isEmpty()) return;
        int frameIndex = (int) (timer/frameDuration) % frames.size();
        Image currentFrame = frames.get(frameIndex);

        double scale = 1.5;
        double w = currentFrame.getWidth()*scale;
        double h = currentFrame.getHeight()*scale;

        double ox = x - (w-Player.getWIDTH())/2;
        double oy = y + Player.getHEIGHT() - h;

        if(flipped){
            gc.drawImage(currentFrame, ox + w, oy, -w, h);
        }else {
            gc.drawImage(currentFrame, ox, oy, w, h);
        }
    }



}
