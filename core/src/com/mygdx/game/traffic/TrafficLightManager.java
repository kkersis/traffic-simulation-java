package com.mygdx.game.traffic;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

public class TrafficLightManager {

    private ArrayList<TrafficLight> trafficLights;
    private int counter = 0;
    private int nextLightCounter = 0;
    private enum TrafficLightState{STRAIGHT_AND_RIGHT_VERTICAL, STRAIGHT_AND_RIGHT_HORIZONTAL, LEFT_VERTICAL, LEFT_HORIZONTAL};
    private static final Color RED = Color.RED;
    private static final Color GREEN = Color.GREEN;
    private static final int LIGHT_INTERVAL = 100;


    public TrafficLightManager(ArrayList<TrafficLight> trafficLights){
        this.trafficLights = trafficLights;
    }

    private void change(TrafficLightState nextState){
        switch (nextState){
            case STRAIGHT_AND_RIGHT_VERTICAL:
                trafficLights.get(0).setLightsColor(RED, RED);
                trafficLights.get(1).setLightsColor(GREEN, RED);
                trafficLights.get(2).setLightsColor(RED, RED);
                trafficLights.get(3).setLightsColor(GREEN, RED);
                break;

            case STRAIGHT_AND_RIGHT_HORIZONTAL:
                trafficLights.get(0).setLightsColor(GREEN, RED);
                trafficLights.get(1).setLightsColor(RED, RED);
                trafficLights.get(2).setLightsColor(GREEN, RED);
                trafficLights.get(3).setLightsColor(RED, RED);
                break;

            case LEFT_VERTICAL:
                trafficLights.get(0).setLightsColor(RED, RED);
                trafficLights.get(1).setLightsColor(RED, GREEN);
                trafficLights.get(2).setLightsColor(RED, RED);
                trafficLights.get(3).setLightsColor(RED, GREEN);
                break;

            case LEFT_HORIZONTAL:
                trafficLights.get(0).setLightsColor(RED, GREEN);
                trafficLights.get(1).setLightsColor(RED, RED);
                trafficLights.get(2).setLightsColor(RED, GREEN);
                trafficLights.get(3).setLightsColor(RED, RED);
                break;
        }
    }

    public void updateLights(){
        if(counter % LIGHT_INTERVAL == 0){
            switch (nextLightCounter){
                case 0:
                    change(TrafficLightState.STRAIGHT_AND_RIGHT_VERTICAL);
                    break;
                case 1:
                    change(TrafficLightState.STRAIGHT_AND_RIGHT_HORIZONTAL);
                    break;
                case 2:
                    change(TrafficLightState.LEFT_VERTICAL);
                    break;
                case 3:
                    change(TrafficLightState.LEFT_HORIZONTAL);
                    break;
            }
            nextLightCounter++;
            if(nextLightCounter == 4){
                nextLightCounter = 0;
                counter = 0;
            }
        }
        counter++;
    }
}
