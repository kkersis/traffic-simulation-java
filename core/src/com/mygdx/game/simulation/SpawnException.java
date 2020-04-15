package com.mygdx.game.simulation;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class SpawnException extends TrafficSimulationException{
    private String direction;
    private int lane;

    public String getDirection() {
        return direction;
    }
    public int getLane() {
        return lane;
    }

    SpawnException(){}
    SpawnException(String direction){
        super("Negalima sukurti mašinos");
        this.direction = direction;
    }
    SpawnException(String direction, int lane){
        super("Negalima sukurti mašinos");
        this.direction = direction;
        this.lane = lane;
    }
}

