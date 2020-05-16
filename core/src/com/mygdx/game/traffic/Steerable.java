package com.mygdx.game.traffic;

public interface Steerable extends Movable{
    boolean steer(boolean direction, float turningSpeed);
}
