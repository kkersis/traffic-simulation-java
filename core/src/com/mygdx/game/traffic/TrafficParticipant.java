package com.mygdx.game.traffic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class TrafficParticipant implements Movable, Serializable {

    transient protected Sprite sprite;
    protected String imageSrc = "car1.png";
    protected float posX;
    protected float posY;
    protected float velocity;
    protected float angle;
    protected float maxVelocity;
    protected boolean needToAccelerate = false;

    private static int participantsCount = 0;

    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

    public TrafficParticipant(){
        this(new Sprite(new Texture("car1.png")), 0, 0, new Vector2(0, 0));
    }
    public TrafficParticipant(Sprite sprite, float velocity, float angle, Vector2 pos){
        this.sprite = sprite;
        this.velocity = velocity;
        this.angle = angle;
        this.sprite.setPosition(pos.x, pos.y);
        this.sprite.setRotation(angle);
        participantsCount++;
    }

    public final void updateSprite(){
        this.sprite = new Sprite(new Texture(this.imageSrc));
        this.sprite.setPosition(posX, posY);
        this.sprite.setRotation(this.angle);
    }

    public final void accelerate(){
        if(Math.abs(velocity) >= maxVelocity){
            if(velocity < 0) velocity = -maxVelocity;
            else velocity = maxVelocity;
            needToAccelerate = false;
        }
        else velocity += (float)1/30*velocity;
        if(velocity == 0) velocity = 1f;
    }

    public static void participantDestroyed(){
        participantsCount--;
    }
    

    public Sprite getSprite() {
        return sprite;
    }
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
    public void setSprite(String imageSrc) {
        sprite = new Sprite(new Texture(imageSrc));
    }
    public void setSprite(String imageSrc, Vector2 pos){
        float tempAngle = sprite.getRotation();
        sprite = new Sprite(new Texture(imageSrc));
        sprite.setPosition(pos.x, pos.y);
        sprite.setRotation(angle);
        this.imageSrc = imageSrc;
    }
    public float getVelocity() {
        return velocity;
    }
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
    public float getAngle() {
        return angle;
    }
    public void setAngle(float angle) {
        this.angle = angle;
    }
    public int getCount(){
        return participantsCount;
    }
    public void setPosition(Vector2 pos){
        getSprite().setPosition(pos.x, pos.y);
    }

    public abstract void setAhead(TrafficParticipant ahead);
    public abstract void setLights(ArrayList<TrafficLight> trafficLights);


    @Override
    public String toString() {
        return "TrafficParticipant{" +
                "sprite=" + sprite +
                ", velocity=" + velocity +
                ", angle=" + angle +
                ", maxVelocity=" + maxVelocity +
                ", needToAccelerate=" + needToAccelerate +
                '}';
    }

}
