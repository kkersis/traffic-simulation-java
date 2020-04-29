package com.mygdx.game.traffic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Pedestrian extends TrafficParticipant{

    public enum PedestrianState{STANDING, MOVE_X, MOVE_Y}
    public enum PedestrianCommand{GO, STOP}

    private PedestrianState pedestrianState;
    private PedestrianCommand pedestrianCommand;

    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }


    public Pedestrian(Sprite sprite, float velocity, float angle, Vector2 pedestrianPos, PedestrianState pedestrianState,
                      PedestrianCommand pedestrianCommand){
        super(sprite, velocity, angle, pedestrianPos);
        this.pedestrianState = pedestrianState;
        this.pedestrianCommand = pedestrianCommand;
        maxVelocity = 1f;
        if(velocity == 0) this.velocity = maxVelocity;
    }
    public Pedestrian(String imageSrc, float velocity, float angle, Vector2 pedestrianPos, PedestrianState pedestrianState,
                      PedestrianCommand pedestrianCommand){
        this(new Sprite(new Texture(imageSrc)), velocity, angle, pedestrianPos, pedestrianState, pedestrianCommand);
    }
    public Pedestrian(){
        this(new Sprite(new Texture("ped.png")), 0, 0, new Vector2(0, 0), PedestrianState.MOVE_X, PedestrianCommand.GO);
    }

    @Override
    public void setAhead(TrafficParticipant ahead) {}


    public Pedestrian.PedestrianState getPedState() {
        return pedestrianState;
    }
    public void setPedState(Pedestrian.PedestrianState pedState) {
        this.pedestrianState = pedState;
    }
    public void setPedCommand(Pedestrian.PedestrianCommand pedCommand) {
        this.pedestrianCommand = pedCommand;
    }
    public Pedestrian.PedestrianCommand getPedCommand() {
        return pedestrianCommand;
    }

    @Override
    public String toString() {
        return "Pedestrian{" +
                "pedestrianState=" + pedestrianState +
                ", pedestrianCommand=" + pedestrianCommand +
                '}';
    }


    public void move(){
        switch (pedestrianState){
            case MOVE_X:
                switch (pedestrianCommand){
                    case GO:
                        getSprite().translate(velocity, 0);
                        break;

                    case STOP:
                        setPedState(PedestrianState.STANDING);
                        break;
                }
                break;

            case MOVE_Y:
                switch (pedestrianCommand){
                    case GO:
                        getSprite().translate(0, velocity);
                        break;

                    case STOP:
                        setPedState(PedestrianState.STANDING);
                        break;
                }
                break;

            case STANDING:
                break;


        }
    }
}
