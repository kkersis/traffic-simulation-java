package com.mygdx.game.traffic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class TrafficParticipant implements Movable {
    
    protected Sprite sprite;
    protected float velocity;
    protected float angle;
    protected float maxVelocity;
    protected boolean needToAccelerate = false;

    private static int participantsCount = 0;

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


    public final void accelerate(){
        if(Math.abs(velocity) >= maxVelocity){
            if(velocity < 0) velocity = -maxVelocity;
            else velocity = maxVelocity;
            needToAccelerate = false;
        }
        else velocity += (float)1/30*velocity;
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
