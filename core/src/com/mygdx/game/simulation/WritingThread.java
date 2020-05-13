package com.mygdx.game.simulation;

public class WritingThread extends Thread{

    SaveData data;

    WritingThread(SaveData data){
        this.data = data;
    }

    public void run(){
        try{
            synchronized (data) {
                ResourceManager.save(data, "save.bin");
            }
        }catch (Exception e){
            System.out.println("Couldn't save!");
        }
    }
}
