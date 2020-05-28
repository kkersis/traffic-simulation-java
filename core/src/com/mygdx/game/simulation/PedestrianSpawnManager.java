package com.mygdx.game.simulation;

import java.io.Serializable;

public class PedestrianSpawnManager implements Serializable {
    private MyGdxGame myGdxGame;
    private final int interval = 700;
    private int counter = interval;


    PedestrianSpawnManager(MyGdxGame myGdxGame){
        this.myGdxGame = myGdxGame;
    }


    public void update(){
        if(interval - counter <= 0){
            counter = 0;
            if(MyGdxGame.getRandomBetween2()){
                if(MyGdxGame.getRandomBetween2()){
                    try {
                        myGdxGame.spawnPedestrianLeft();
                    }catch (CloneNotSupportedException e){
                        e.printStackTrace();
                    }
                }else{
                    try {
                        myGdxGame.spawnPedestrianUp();
                    }catch (CloneNotSupportedException e){
                        e.printStackTrace();
                    }
                }
            }else{
                if(MyGdxGame.getRandomBetween2()){
                    try {
                        myGdxGame.spawnPedestrianRight();
                    }catch (CloneNotSupportedException e){
                        e.printStackTrace();
                    }
                }else{
                    try {
                        myGdxGame.spawnPedestrianDown();
                    }catch (CloneNotSupportedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        counter++;
    }
}
