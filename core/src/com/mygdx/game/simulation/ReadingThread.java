package com.mygdx.game.simulation;

public class ReadingThread extends Thread{
    SaveData data = new SaveData();
    public void run(){
        try {
            synchronized (data) {
                data = (SaveData) ResourceManager.load("save.bin");
            }
        }catch (Exception e){
            System.out.println("Couldn't load!");
        }
    }

    public SaveData getData(){
        return data;
    }
}
