package com.mygdx.game.traffic;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;

public class PedestriansCrossingManager {
    private ArrayList<Pedestrian> left = new ArrayList<>();
    private ArrayList<Pedestrian> up = new ArrayList<>();
    private ArrayList<Pedestrian> right = new ArrayList<>();
    private ArrayList<Pedestrian> down = new ArrayList<>();
    public boolean isLeftUP;
    public boolean isLeftDOWN;
    public boolean isUpLEFT;
    public boolean isUpRIGHT;
    public boolean isRightUP;
    public boolean isRightDOWN;
    public boolean isDownLEFT;
    public boolean isDownRIGHT;
    private float pos;
    private final float pos1 = 255; //situs pakeist jei pestieji kirsis su masinomis
    private final float pos2 = 465; //

    private final float pos3 = 455; //
    private final float pos4 = 655; //


    public void check(){
        for(Pedestrian pedestrian : left){
            pos = pedestrian.getSprite().getY();
            isLeftUP = false;
            isLeftDOWN = false;
            if(PedPosBetweenY(pos, pos1, pos2)){
                isLeftDOWN = true;
                break;
            }
            if(PedPosBetweenY(pos, pos3, pos4)){
                isLeftUP = true;
                break;
            }
        }

        for(Pedestrian pedestrian : up){
            pos = pedestrian.getSprite().getX();
            isUpLEFT = false;
            isUpRIGHT = false;
            if(PedPosBetweenX(pos, pos1, pos2)){
                isUpLEFT = true;
                break;
            }
            if(PedPosBetweenX(pos, pos3, pos4)){
                isUpRIGHT = true;
                break;
            }
        }

        for(Pedestrian pedestrian : right){
            pos = pedestrian.getSprite().getY();
            isRightUP = false;
            isRightDOWN = false;
            if(PedPosBetweenY(pos, pos1, pos2)){
                isRightDOWN = true;
                break;
            }
            if(PedPosBetweenY(pos, pos3, pos4)){
                isRightUP = true;
                break;
            }
        }

        for(Pedestrian pedestrian : down){
            pos = pedestrian.getSprite().getX();
            isDownRIGHT = false;
            isDownLEFT = false;
            if(PedPosBetweenX(pos, pos1, pos2)){
                isDownLEFT = true;
                break;
            }
            if(PedPosBetweenX(pos, pos3, pos4)){
                isDownRIGHT = true;
                break;
            }
        }

    }

    public void addLeft(Pedestrian ped){
        this.left.add(ped);
    }
    public void addUp(Pedestrian ped){
        this.up.add(ped);
    }
    public void addRight(Pedestrian ped){
        this.right.add(ped);
    }
    public void addDown(Pedestrian ped){
        this.down.add(ped);
    }

    public boolean isLeftUP() {
        return isLeftUP;
    }

    public boolean isLeftDOWN() {
        return isLeftDOWN;
    }

    public boolean isUpLEFT() {
        return isUpLEFT;
    }

    public boolean isUpRIGHT() {
        return isUpRIGHT;
    }

    public boolean isRightUP() {
        return isRightUP;
    }

    public boolean isRightDOWN() {
        return isRightDOWN;
    }

    public boolean isDownLEFT() {
        return isDownLEFT;
    }

    public boolean isDownRIGHT() {
        return isDownRIGHT;
    }

    private boolean PedPosBetweenX(float posX, float x1, float x2){
        if(x1 < x2) return x1 <= posX && posX <= x2;
        else return x2 <= posX && posX <= x1;
    }

    private boolean PedPosBetweenY(float posY, float y1, float y2){
        if(y1 < y2) return y1 <= posY && posY <= y2;
        else return y2 <= posY && posY <= y1;
    }
}
