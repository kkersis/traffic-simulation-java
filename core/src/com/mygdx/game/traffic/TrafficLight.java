package com.mygdx.game.traffic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TrafficLight{
    private Sprite sprite;
    private float angle;
    private Color straightRightColor;
    private Color leftColor;

    public TrafficLight(){
        sprite = new Sprite(new Texture("trafficLight.png"));
    }

    public Sprite getSprite(){
        return sprite;
    }
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
    public void setSprite(String imageSrc) {
        sprite = new Sprite(new Texture(imageSrc));
    }
    public float getAngle() {
        return angle;
    }
    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Color getStraightRightColor(){
        return straightRightColor;
    }
    public void setStraightRightColor(Color col){
        straightRightColor = col;
    }
    public Color getLeftColor(){
        return leftColor;
    }
    public void setLeftColor(Color col){
        leftColor = col;
    }
    public void setLightsColor(Color straightRightColor, Color leftColor){
        this.straightRightColor = straightRightColor;
        this.leftColor = leftColor;
    }
}
