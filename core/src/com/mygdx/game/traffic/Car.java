package com.mygdx.game.traffic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.ArrayList;

import static java.lang.Math.*;

public class Car extends TrafficParticipant implements Steerable, Cloneable, Serializable {
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
    private ArrayList<TrafficLight> trafficLights;
    private int lane;
    private boolean madeTurn = false;
    private PedestriansCrossingManager pedCrossingManager;
    private boolean isWaiting;
    private boolean canTurn;


    @Override
    public Object clone() throws CloneNotSupportedException{
        Car car = (Car) super.clone();
        if(carAhead != null){
            car.setAhead((Car) carAhead.clone());
        }
        if(trafficLights != null){
            car.setLights(trafficLights);
        }
        if(pedCrossingManager != null){
            car.setCrossingManager(pedCrossingManager);
        }
        return car;
    }

    public boolean steer(boolean dir, float turningSpeed) {
        cosSpeed = velocity * (float)Math.cos(toRadians(anglePassed));  //pradzioj 4*1 = 4
        sinSpeed = velocity * (float)Math.sin(toRadians(anglePassed));  //pradzioj 4*0 = 0

        getSprite().setRotation(angle);

        if(dir)
            turningSpeed *= -1;

        angle += turningSpeed;

        anglePassed += Math.abs(turningSpeed);
        if(anglePassed >= 90){
            anglePassed = 0;
            madeTurn = true;
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
               CarState carState, Car carAhead, ArrayList<TrafficLight> trafficLights, int lane, PedestriansCrossingManager pedestriansCrossingManager) {
        super(sprite, velocity, angle, carPos);
        this.carDirection = carDirection;
        this.carState = carState;
        this.prevState = carState;
        this.carAhead = carAhead;
        this.trafficLights = trafficLights;
        this.lane = lane;
        this.pedCrossingManager = pedestriansCrossingManager;
        maxVelocity = 8f;
        carCount++;
        if(velocity == 0) super.velocity = maxVelocity;
    }
    public Car(String imageSrc, float velocity, float angle, Vector2 carPos, CarDirection carDirection,
               CarState carState, Car carAhead, ArrayList<TrafficLight> trafficLights, int lane, PedestriansCrossingManager pedestriansCrossingManager){
        this(new Sprite(new Texture(imageSrc)), velocity, angle, carPos, carDirection, carState, carAhead, trafficLights, lane, pedestriansCrossingManager);
        this.imageSrc = imageSrc;
    }
    public Car(){
        this("car1.png", 0, 0, new Vector2(0, 0), CarDirection.LEFT, CarState.BRAKE, null, null, 1, null);
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
    public void setLights(ArrayList<TrafficLight> trafficLights){
        this.trafficLights = trafficLights;
    }

    public void setLane(int lane){
        this.lane = lane;
    }

    public void setCrossingManager(PedestriansCrossingManager pedCrossingManager){
        this.pedCrossingManager = pedCrossingManager;
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

    private boolean carPosBetweenX(float x1, float x2){
        if(x1 < x2) return x1 <= posX && posX <= x2;
        else return x2 <= posX && posX <= x1;
    }

    private boolean carPosBetweenY(float y1, float y2){
        if(y1 < y2) return y1 <= posY && posY <= y2;
        else return y2 <= posY && posY <= y1;
    }

    private boolean carAheadWaiting(){
        if(carAhead != null){
            return carAhead.isWaiting;
        }
        return false;
    }

    public void move() {

        if(needToAccelerate) accelerate();

        this.posX = sprite.getX();
        this.posY = sprite.getY();

        TrafficLight trafficLight;
        float tLPos;

        switch(carState) {
            case BRAKE:
                break;
            case MOVE_X:
                switch (carDirection) {
                    case LEFT:
                        velocity = -Math.abs(velocity);
                        trafficLight = trafficLights.get(2);
                        tLPos = trafficLight.getSprite().getX();
                        if(!madeTurn) {
                            switch (lane) {
                                case 1:
                                    if (carPosBetweenX(tLPos + 30, tLPos + 40) && (trafficLight.getLeftColor() == Color.RED || pedCrossingManager.isRightUP())) {
                                        velocity = 0;
                                    }
                                    if (velocity == 0 && trafficLight.getLeftColor() == Color.GREEN && !pedCrossingManager.isRightUP()) {
                                        needToAccelerate = true;
                                        velocity = -1f;
                                    }
                                    if (posX <= tLPos - 35) {
                                        velocity = -maxVelocity;
                                        setCarState(CarState.TURNING);
                                        setCommand(Command.TURN_LEFT);
                                        setCarDirection(CarDirection.LEFT_DOWN);
                                    }
                                    break;
                                case 2:
                                    if ((velocity == 0 && trafficLight.getStraightRightColor() == Color.GREEN && !pedCrossingManager.isRightUP()) || (velocity == 0 && !pedCrossingManager.isLeftUP() && posX < tLPos - 50)) {
                                        needToAccelerate = true;
                                        velocity = -1;
                                    }
                                    if (carPosBetweenX(tLPos + 30, tLPos + 40) && (trafficLight.getStraightRightColor() == Color.RED || pedCrossingManager.isRightUP())) {
                                        velocity = 0;
                                    }
                                    if(carPosBetweenX(300, 310) && pedCrossingManager.isLeftUP()){
                                        velocity = 0;
                                    }
                                    break;
                                case 3:
                                    if (carPosBetweenX(tLPos + 30, tLPos + 40) && (trafficLight.getStraightRightColor() == Color.RED || pedCrossingManager.isRightUP())) {
                                        velocity = 0;
                                    }
                                    if (velocity == 0 && trafficLight.getStraightRightColor() == Color.GREEN && !carAheadWaiting()  && !pedCrossingManager.isRightUP()) {
                                        needToAccelerate = true;
                                        velocity = -1;
                                    }
                                    if (posX <= tLPos - 25) {
                                        velocity = -maxVelocity;
                                        setCarState(CarState.TURNING);
                                        setCommand(Command.TURN_RIGHT);
                                        setCarDirection(CarDirection.LEFT_UP);
                                    }
                                    break;
                            }
                            if(carAhead != null){
                                if(carPosBetweenX(carAhead.posX+50, carAhead.posX+110)){
                                    velocity = 0;
                                }
                            }
                        }
                        break;
                    case RIGHT:
                        velocity = Math.abs(velocity);
                        trafficLight = trafficLights.get(0);
                        tLPos = trafficLight.getSprite().getX();
                        if(!madeTurn) {
                            switch (lane) {
                                case 1:
                                    if (carPosBetweenX(tLPos - 55, tLPos - 45) && (trafficLight.getLeftColor() == Color.RED || pedCrossingManager.isLeftDOWN())) {
                                        velocity = 0;
                                    }
                                    if (velocity == 0 && trafficLight.getLeftColor() == Color.GREEN && !pedCrossingManager.isLeftDOWN()) {
                                        needToAccelerate = true;
                                        velocity = 1;
                                    }
                                    if (posX >= tLPos + 15) {
                                        velocity = maxVelocity;
                                        setCarState(CarState.TURNING);
                                        setCommand(Command.TURN_LEFT);
                                        setCarDirection(CarDirection.RIGHT_UP);
                                    }
                                    break;
                                case 2:
                                    if (velocity == 0 && trafficLight.getStraightRightColor() == Color.GREEN && !pedCrossingManager.isLeftDOWN() || (velocity == 0 && !pedCrossingManager.isRightDOWN() && posX > tLPos + 50)) {
                                        needToAccelerate = true;
                                        velocity = 1;
                                    }
                                    if (carPosBetweenX(tLPos - 55, tLPos - 45) && (trafficLight.getStraightRightColor() == Color.RED || pedCrossingManager.isLeftDOWN())) {
                                        velocity = 0;
                                    }
                                    if(carPosBetweenX(590, 600) && pedCrossingManager.isRightDOWN()){
                                        velocity = 0;
                                    }
                                    break;
                                case 3:
                                    if (carPosBetweenX(tLPos - 55, tLPos - 45) && (trafficLight.getStraightRightColor() == Color.RED || pedCrossingManager.isLeftDOWN())) {
                                        velocity = 0;
                                    }
                                    if (velocity == 0 && trafficLight.getStraightRightColor() == Color.GREEN && !carAheadWaiting() && !pedCrossingManager.isLeftDOWN()) {
                                        needToAccelerate = true;
                                        velocity = 1;
                                    }
                                    if (posX >= tLPos + 5) {
                                        velocity = maxVelocity;
                                        setCarState(CarState.TURNING);
                                        setCommand(Command.TURN_RIGHT);
                                        setCarDirection(CarDirection.RIGHT_DOWN);
                                    }
                                    break;
                            }
                            if(carAhead != null){
                                if(carPosBetweenX(carAhead.posX-110, carAhead.posX-50)){
                                    velocity = 0;
                                }
                            }
                        }
                        break;
                }
                getSprite().translate(velocity, 0);

                break;
            case MOVE_Y:
                if(carAheadWaiting()) isWaiting = true;
                else isWaiting = false;
                switch (carDirection){
                    case UP:
                        velocity = Math.abs(velocity);
                        trafficLight = trafficLights.get(3);
                        tLPos = trafficLight.getSprite().getY();
                        if(!madeTurn) {
                            switch (lane) {
                                case 1:
                                    if (carPosBetweenY(tLPos - 55, tLPos - 45) && (trafficLight.getLeftColor() == Color.RED || pedCrossingManager.isDownRIGHT())) {
                                        velocity = 0;
                                    }
                                    if (velocity == 0 && trafficLight.getLeftColor() == Color.GREEN && !pedCrossingManager.isDownRIGHT) {
                                        needToAccelerate = true;
                                        velocity = 1;
                                    }
                                    if (posY >= tLPos + 15) {
                                        velocity = maxVelocity;
                                        setCarState(CarState.TURNING);
                                        setCommand(Command.TURN_LEFT);
                                        setCarDirection(CarDirection.UP_LEFT);
                                    }
                                    break;
                                case 2:
                                    if ((velocity == 0 && trafficLight.getStraightRightColor() == Color.GREEN && !pedCrossingManager.isDownRIGHT) || (velocity == 0 && !pedCrossingManager.isUpRIGHT() && posY > tLPos + 50)) {
                                        needToAccelerate = true;
                                        velocity = 1;
                                    }
                                    if (carPosBetweenY(tLPos - 55, tLPos - 45) && (trafficLight.getStraightRightColor() == Color.RED || pedCrossingManager.isDownRIGHT())) {
                                        velocity = 0;
                                    }
                                    if(carPosBetweenY(610, 620) && pedCrossingManager.isUpRIGHT()){
                                        velocity = 0;
                                    }
                                    break;
                                case 3:
                                    if (carPosBetweenY(tLPos - 55, tLPos - 45) && (trafficLight.getStraightRightColor() == Color.RED || pedCrossingManager.isDownRIGHT())) {
                                        velocity = 0;
                                    }
                                    if (velocity == 0 && trafficLight.getStraightRightColor() == Color.GREEN && !carAheadWaiting() && !pedCrossingManager.isDownRIGHT) {
                                        needToAccelerate = true;
                                        velocity = 1;
                                    }
                                    if (posY >= tLPos + 5) {
                                        velocity = maxVelocity;
                                        setCarState(CarState.TURNING);
                                        setCommand(Command.TURN_RIGHT);
                                        setCarDirection(CarDirection.UP_RIGHT);
                                    }
                                    break;
                            }
                            if(carAhead != null){
                                if(carPosBetweenY(carAhead.posY-110, carAhead.posY)){
                                    velocity = 0;
                                    break;
                                }
                            }
                        }
                        break;
                    case DOWN:
                        velocity = -Math.abs(velocity);
                        trafficLight = trafficLights.get(1);
                        tLPos = trafficLight.getSprite().getY();
                        if(!madeTurn) {
                            switch (lane) {
                                case 1:
                                    if (carPosBetweenY(tLPos + 30, tLPos + 40) && (trafficLight.getLeftColor() == Color.RED || pedCrossingManager.isUpLEFT())) {
                                        velocity = 0;
                                    }
                                    if (velocity == 0 && trafficLight.getLeftColor() == Color.GREEN && !pedCrossingManager.isUpLEFT()) {
                                        needToAccelerate = true;
                                        velocity = -1;
                                    }
                                    if (posY <= tLPos - 25) {
                                        velocity = -maxVelocity;
                                        setCarState(CarState.TURNING);
                                        setCommand(Command.TURN_LEFT);
                                        setCarDirection(CarDirection.DOWN_RIGHT);
                                    }
                                    break;
                                case 2:
                                    if ((velocity == 0 && trafficLight.getStraightRightColor() == Color.GREEN && !pedCrossingManager.isUpLEFT()) || (velocity == 0 && !pedCrossingManager.isDownLEFT() && posY < tLPos - 50)) {
                                        needToAccelerate = true;
                                        velocity = -1;
                                    }
                                    if (carPosBetweenY(tLPos + 30, tLPos + 40) && (trafficLight.getStraightRightColor() == Color.RED || pedCrossingManager.isUpLEFT())) {
                                        velocity = 0;
                                    }
                                    if(carPosBetweenY(330, 340) && pedCrossingManager.isDownLEFT()){
                                        velocity = 0;
                                    }
                                    break;
                                case 3:
                                    if (carPosBetweenY(tLPos + 30, tLPos + 40) && (trafficLight.getStraightRightColor() == Color.RED || pedCrossingManager.isUpLEFT())) {
                                        velocity = 0;
                                    }
                                    if (velocity == 0 && trafficLight.getStraightRightColor() == Color.GREEN && !carAheadWaiting() && !pedCrossingManager.isUpLEFT()) {
                                        needToAccelerate = true;
                                        velocity = -1;
                                    }
                                    if(carAheadWaiting()){
                                        needToAccelerate=false;
                                        velocity = 0;
                                    }
                                    if (posY <= tLPos - 25) {
                                        velocity = -maxVelocity;
                                        setCarState(CarState.TURNING);
                                        setCommand(Command.TURN_RIGHT);
                                        setCarDirection(CarDirection.DOWN_LEFT);
                                    }
                                    break;
                            }
                            if(carAhead != null){
                                if(carPosBetweenY(carAhead.posY+110, carAhead.posY+50)){
                                    velocity = 0;
                                }
                            }
                        }
                        break;
                }
                getSprite().translate(0, velocity);
                break;
            case TURNING:
                if(!isWaiting) {
                    decelerate((float) 1 / 2 * maxVelocity, (float) 1 / 30);
                }
                if(carAheadWaiting()) isWaiting = true;
                else isWaiting = false;

                switch(command) {
                    case TURN_LEFT:
                        switch (carDirection){
                            case UP_LEFT:
                                if(Math.abs(velocity) < 1) velocity = maxVelocity;
                                if(!(pedCrossingManager.isLeftUP() && carPosBetweenX(305, 315)) && !carAheadWaiting()) {
                                    if (steer(false, 1f)) {
                                        setCommand(Command.GO_STRAIGHT);
                                        setCarDirection(CarDirection.LEFT);
                                        setCarState(CarState.MOVE_X);
                                        angle = 180;
                                        getSprite().setRotation(angle);
                                    }
                                }else{
                                    isWaiting = true;
                                    cosSpeed = 0;
                                    sinSpeed = 0;
                                }
                                getSprite().translate(-sinSpeed, cosSpeed);
                                break;
                            case RIGHT_UP:
                                if(Math.abs(velocity) < 1) velocity = maxVelocity;
                                if(!(pedCrossingManager.isUpRIGHT() && carPosBetweenY(620, 630)) && !carAheadWaiting()) {
                                    if (steer(false, 1f)) {
                                        setCommand(Command.GO_STRAIGHT);
                                        setCarDirection(CarDirection.UP);
                                        setCarState(CarState.MOVE_Y);
                                        angle = 90;
                                        getSprite().setRotation(angle);
                                    }
                                }else{
                                    isWaiting = true;
                                    cosSpeed = 0;
                                    sinSpeed = 0;
                                }
                                getSprite().translate(cosSpeed, sinSpeed);
                                break;
                            case DOWN_RIGHT:
                                if(Math.abs(velocity) < 1) velocity = -maxVelocity;
                                if(!(pedCrossingManager.isRightDOWN() && carPosBetweenX(580, 590)) && !carAheadWaiting()) {
                                    if (steer(false, 1f)) {
                                        setCommand(Command.GO_STRAIGHT);
                                        setCarDirection(CarDirection.RIGHT);
                                        setCarState(CarState.MOVE_X);
                                        angle = 0;
                                        getSprite().setRotation(angle);
                                    }
                                }else{
                                    isWaiting = true;
                                    cosSpeed = 0;
                                    sinSpeed = 0;
                                }
                                getSprite().translate(-sinSpeed, cosSpeed);
                                break;
                            case LEFT_DOWN:
                                if(Math.abs(velocity) < 1) velocity = -maxVelocity;
                                if(!(pedCrossingManager.isDownLEFT() && carPosBetweenY(330, 340)) && !carAheadWaiting()) {
                                    if (steer(false, 1f)) {
                                        setCommand(Command.GO_STRAIGHT);
                                        setCarDirection(CarDirection.DOWN);
                                        setCarState(CarState.MOVE_Y);
                                        angle = 270;
                                        getSprite().setRotation(angle);
                                    }
                                }else{
                                    isWaiting = true;
                                    cosSpeed = 0;
                                    sinSpeed = 0;
                                }
                                getSprite().translate(cosSpeed, sinSpeed);
                                break;
                        }
                        break;
                    case TURN_RIGHT:
                        switch(carDirection) {
                            case UP_RIGHT:
                                if(Math.abs(velocity) < 1) velocity = maxVelocity;
                                if(!(pedCrossingManager.isRightDOWN() && carPosBetweenY(270, 280))) {
                                    if (steer(true, 2f)) {
                                        setCommand(Command.GO_STRAIGHT);
                                        setCarDirection(CarDirection.RIGHT);
                                        setCarState(CarState.MOVE_X);
                                        angle = 0;
                                        getSprite().setRotation(angle);
                                    }
                                }else{
                                    isWaiting = true;
                                    cosSpeed = 0;
                                    sinSpeed = 0;
                                }
                                getSprite().translate(sinSpeed, cosSpeed);
                                break;
                            case RIGHT_DOWN:
                                if(Math.abs(velocity) < 1) velocity = maxVelocity;
                                if(!(pedCrossingManager.isDownLEFT() && carPosBetweenX(270, 280))){
                                    if(steer(true, 2f)){
                                        setCommand(Command.GO_STRAIGHT);
                                        setCarDirection(CarDirection.DOWN);
                                        setCarState(CarState.MOVE_Y);
                                        angle = 270;
                                        getSprite().setRotation(angle);
                                    }
                                }else{
                                    isWaiting = true;
                                    cosSpeed = 0;
                                    sinSpeed = 0;
                                }
                                getSprite().translate(cosSpeed, -sinSpeed);
                                break;
                            case DOWN_LEFT:
                                if(Math.abs(velocity) < 1) velocity = -maxVelocity;
                                if(!(pedCrossingManager.isLeftUP() && carPosBetweenY(650, 660))) {
                                    if (steer(true, 2f)) {
                                        setCommand(Command.GO_STRAIGHT);
                                        setCarDirection(CarDirection.LEFT);
                                        setCarState(CarState.MOVE_X);
                                        angle = 180;
                                        getSprite().setRotation(angle);
                                    }
                                }else{
                                    isWaiting = true;
                                    cosSpeed = 0;
                                    sinSpeed = 0;
                                }
                                getSprite().translate(sinSpeed, cosSpeed);
                                break;
                            case LEFT_UP:
                                if(Math.abs(velocity) < 1) velocity = -maxVelocity;
                                if(!(pedCrossingManager.isUpRIGHT() && carPosBetweenX(650, 660))) {
                                    if (steer(true, 2f)) {
                                        setCommand(Command.GO_STRAIGHT);
                                        setCarDirection(CarDirection.UP);
                                        setCarState(CarState.MOVE_Y);
                                        angle = 90;
                                        getSprite().setRotation(angle);
                                    }
                                }else{
                                    isWaiting = true;
                                    cosSpeed = 0;
                                    sinSpeed = 0;
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

