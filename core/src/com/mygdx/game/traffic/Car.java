package com.mygdx.game.traffic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.*;

public class Car extends TrafficParticipant implements Steerable, Cloneable{
    public enum CarDirection{LEFT, RIGHT, UP, DOWN, DOWN_LEFT,RIGHT_DOWN,
        LEFT_UP, UP_LEFT, UP_RIGHT, RIGHT_UP, DOWN_RIGHT, LEFT_DOWN}
    public enum CarState{BRAKE, MOVE_X, MOVE_Y, TURNING}
    public enum Command{GO_STRAIGHT, TURN_LEFT, TURN_RIGHT}


    private static int carCount = 0;
    private CarDirection carDirection;
    private CarState carState;
    private Command command = Command.GO_STRAIGHT;
    private float anglePassed = 0;
    private CarState prevState;
    private float cosSpeed;
    private float sinSpeed;
    private Car carAhead;


    @Override
    public Object clone() throws CloneNotSupportedException{
        Car car = (Car) super.clone();
        if(carAhead != null){
            car.setAhead((Car) carAhead.clone());
        }
        return car;
    }

    public boolean steer(boolean dir) {
        cosSpeed = velocity * (float)Math.cos(toRadians(anglePassed));  //pradzioj 4*1 = 4
        sinSpeed = velocity * (float)Math.sin(toRadians(anglePassed));  //pradzioj 4*0 = 0

        getSprite().setRotation(angle);
        float turningSpeed = 2f;

        if(dir)
            turningSpeed *= -1;

        angle += turningSpeed;

        anglePassed += Math.abs(turningSpeed);
        if(anglePassed >= 90){
            anglePassed = 0;
            return true;
        }
        return false;
    }

    private void decelerate(float untilVel, float decSpeed){
        if(Math.abs(velocity) > untilVel){
            velocity -= decSpeed*velocity;
        }
    }


    public static void carDestroyed() {
        carCount--;
    }


    public Car(Sprite sprite, float velocity, float angle, Vector2 carPos, CarDirection carDirection,
               CarState carState, Car carAhead) {
        super(sprite, velocity, angle, carPos);
        this.carDirection = carDirection;
        this.carState = carState;
        this.prevState = carState;
        this.carAhead = carAhead;
        maxVelocity = 8f;
        carCount++;
        if(velocity == 0) super.velocity = maxVelocity;
    }
    public Car(String imageSrc, float velocity, float angle, Vector2 carPos, CarDirection carDirection,
               CarState carState, Car carAhead){
        this(new Sprite(new Texture(imageSrc)), velocity, angle, carPos, carDirection, carState, carAhead);
    }
    public Car(){
        this("car1.png", 0, 0, new Vector2(0, 0), CarDirection.LEFT, CarState.BRAKE, null);
    }


    
    public CarDirection getCarDirection() {
        return carDirection;
    }
    public void setCarDirection(CarDirection carDirection) {
        this.carDirection = carDirection;
    }
    public CarState getCarState() {
        return carState;
    }
    public void setCarState(CarState carState) {
        prevState = this.carState;
        this.carState = carState;
        if(prevState == CarState.TURNING){
            needToAccelerate = true; //masina baige sukimasi, greitis didinamas i iprasta
        }
    }
    public void setCommand(Command command) {
        this.command = command;
    }
    public Command getCommand() {
        return command;
    }



    @Override
    public int getCount()
    {
        return carCount;
    }

    @Override
    public void setAhead(TrafficParticipant ahead) {
        this.carAhead = (Car)ahead;
    }

    @Override
    public String toString() {
        return super.toString() + "Car{" +
                "carDirection=" + carDirection +
                ", carState=" + carState +
                ", command=" + command +
                ", anglePassed=" + anglePassed +
                ", prevState=" + prevState +
                ", cosSpeed=" + cosSpeed +
                ", sinSpeed=" + sinSpeed +
                '}';
    }


    public void move() {

        if(needToAccelerate) accelerate();

        switch(carState) {
            case BRAKE:
                break;
            case MOVE_X:
                if(carDirection == CarDirection.LEFT) {
                    velocity = -Math.abs(velocity);
                } else if(carDirection == CarDirection.RIGHT) {
                    velocity = Math.abs(velocity);
                }
                getSprite().translate(velocity, 0);

                break;
            case MOVE_Y:
                if(carDirection == CarDirection.UP) {
                    velocity = Math.abs(velocity);
                } else if(carDirection == CarDirection.DOWN) {
                    velocity = -Math.abs(velocity);
                }
                getSprite().translate(0, velocity);
                break;
            case TURNING:
                decelerate((float)1/2*maxVelocity, (float)1/30);

                switch(command) {
                    case TURN_LEFT:
                        switch (carDirection){
                            case UP_LEFT:
                                if(steer(false)){
                                    setCommand(Command.GO_STRAIGHT);
                                    setCarDirection(CarDirection.LEFT);
                                    setCarState(CarState.MOVE_X);
                                    angle = 180;
                                    getSprite().setRotation(angle);
                                }
                                getSprite().translate(-sinSpeed, cosSpeed);
                                break;
                            case RIGHT_UP:
                                if(steer(false)){
                                    setCommand(Command.GO_STRAIGHT);
                                    setCarDirection(CarDirection.UP);
                                    setCarState(CarState.MOVE_Y);
                                    angle = 90;
                                    getSprite().setRotation(angle);
                                }
                                getSprite().translate(cosSpeed, sinSpeed);
                                break;
                            case DOWN_RIGHT:
                                if(steer(false)){
                                    setCommand(Command.GO_STRAIGHT);
                                    setCarDirection(CarDirection.RIGHT);
                                    setCarState(CarState.MOVE_X);
                                    angle = 0;
                                    getSprite().setRotation(angle);
                                }
                                getSprite().translate(-sinSpeed, cosSpeed);
                                break;
                            case LEFT_DOWN:
                                if(steer(false)){
                                    setCommand(Command.GO_STRAIGHT);
                                    setCarDirection(CarDirection.DOWN);
                                    setCarState(CarState.MOVE_Y);
                                    angle = 270;
                                    getSprite().setRotation(angle);
                                }
                                getSprite().translate(cosSpeed, sinSpeed);
                                break;
                        }
                        break;
                    case TURN_RIGHT:
                        switch(carDirection) {
                            case UP_RIGHT:
                                if(steer(true)){
                                    setCommand(Command.GO_STRAIGHT);
                                    setCarDirection(CarDirection.RIGHT);
                                    setCarState(CarState.MOVE_X);
                                    angle = 0;
                                    getSprite().setRotation(angle);
                                }
                                getSprite().translate(sinSpeed, cosSpeed);
                                break;
                            case RIGHT_DOWN:
                                if(steer(true)){
                                    setCommand(Command.GO_STRAIGHT);
                                    setCarDirection(CarDirection.DOWN);
                                    setCarState(CarState.MOVE_Y);
                                    angle = 270;
                                    getSprite().setRotation(angle);
                                }
                                getSprite().translate(cosSpeed, -sinSpeed);
                                break;
                            case DOWN_LEFT:
                                if(steer(true)){
                                    setCommand(Command.GO_STRAIGHT);
                                    setCarDirection(CarDirection.LEFT);
                                    setCarState(CarState.MOVE_X);
                                    angle = 180;
                                    getSprite().setRotation(angle);
                                }
                                getSprite().translate(sinSpeed, cosSpeed);
                                break;
                            case LEFT_UP:
                                if(steer(true)){
                                    setCommand(Command.GO_STRAIGHT);
                                    setCarDirection(CarDirection.UP);
                                    setCarState(CarState.MOVE_Y);
                                    angle = 90;
                                    getSprite().setRotation(angle);
                                }
                                getSprite().translate(cosSpeed, -sinSpeed);
                                break;
                            default:
                                break;
                        }
                        break;
                    case GO_STRAIGHT:
                        break;
                }
        }
    }


}

