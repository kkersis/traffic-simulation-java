package com.mygdx.game.simulation;

import com.mygdx.game.traffic.Car;

import java.io.Serializable;


public class CarsSpawnManager implements Serializable {
    final private Car[] leftLast = {null, null, null};    //left, mid, right
    final private Car[] upLast = {null, null, null};    //left, mid, right
    final private Car[] rightLast = {null, null, null};    //left, mid, right
    final private Car[] downLast = {null, null, null};    //left, mid, right


    public Car getLeftLast(int place){
        return this.leftLast[place];
    }

    public void setLeftLast(int place, Car car) {
        this.leftLast[place] = car;
    }

    public Car getUpLast(int place) {
        return upLast[place];
    }

    public void setUpLast(int place, Car car) {
        this.upLast[place] = car;
    }

    public Car getRightLast(int place) {
        return rightLast[place];
    }

    public void setRightLast(int place, Car car) {
        this.rightLast[place] = car;
    }

    public Car getDownLast(int place) {
        return downLast[place];
    }

    public void setDownLast(int place, Car car) {
        this.downLast[place] = car;
    }


}


