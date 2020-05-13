package com.mygdx.game.simulation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.traffic.*;

import java.io.Serializable;
import java.util.ArrayList;

public class SaveData implements Serializable {
    private TrafficLightManager trafficLightManager;
    private CarsSpawnManager carsSpawnManager;
    private ArrayList<Car> cars;
    private ArrayList<Pedestrian> pedestrians;
    private ArrayList<TrafficParticipant> trafficParticipants;
    private ArrayList<TrafficLight> trafficLights;

    public void setData(TrafficLightManager trafficLightManager, CarsSpawnManager carsSpawnManager, ArrayList<Car> cars, ArrayList<Pedestrian> pedestrians,
                        ArrayList<TrafficParticipant> trafficParticipants){
        this.trafficLightManager = trafficLightManager;
        this.carsSpawnManager = carsSpawnManager;
        this.cars = cars;
        this.pedestrians = pedestrians;
        this.trafficParticipants = trafficParticipants;
    }

    public TrafficLightManager getTrafficLightManager() {
        return trafficLightManager;
    }

    public CarsSpawnManager getCarsSpawnManager() {
        return carsSpawnManager;
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

    public ArrayList<TrafficLight> getTrafficLights() {
        return trafficLights;
    }
}
