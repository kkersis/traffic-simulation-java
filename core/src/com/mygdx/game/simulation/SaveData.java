package com.mygdx.game.simulation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.traffic.*;

import java.io.Serializable;
import java.util.ArrayList;

public class SaveData implements Serializable {
    private TrafficLightManager trafficLightManager;
    private ArrayList<Car> cars;
    private ArrayList<Pedestrian> pedestrians;
    private ArrayList<TrafficParticipant> trafficParticipants;
    private PedestriansCrossingManager pedestriansCrossingManager;

    public void setData(TrafficLightManager trafficLightManager, ArrayList<Car> cars, ArrayList<Pedestrian> pedestrians,
                        ArrayList<TrafficParticipant> trafficParticipants, PedestriansCrossingManager pedestriansCrossingManager){
        this.trafficLightManager = trafficLightManager;

        this.cars = cars;
        this.pedestrians = pedestrians;
        this.trafficParticipants = trafficParticipants;
        this.pedestriansCrossingManager = pedestriansCrossingManager;
    }

    public TrafficLightManager getTrafficLightManager() {
        return trafficLightManager;
    }


    public ArrayList<Car> getCars() {
        return cars;
    }

    public ArrayList<Pedestrian> getPedestrians() {
        return pedestrians;
    }

    public ArrayList<TrafficParticipant> getTrafficParticipants() {
        return trafficParticipants;
    }

    public PedestriansCrossingManager getPedestriansCrossingManager(){
        return pedestriansCrossingManager;
    }


}
