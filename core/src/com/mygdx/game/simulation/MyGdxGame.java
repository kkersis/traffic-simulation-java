package com.mygdx.game.simulation;

import com.mygdx.game.traffic.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
	private SpriteBatch batch;
	private Texture backgroundImage;
	private TrafficParticipant participant;
	private ShapeRenderer shapeRenderer;
	private TrafficLightManager trafficLightManager;
	private CarsSpawnManager carsSpawnManager;
	private ArrayList<Car> cars = new ArrayList<>();
	private ArrayList<Pedestrian> pedestrians = new ArrayList<>();
	private ArrayList<TrafficParticipant> trafficParticipants = new ArrayList<>();
	private ArrayList<TrafficLight> trafficLights = new ArrayList<>();
	private SaveData data = new SaveData();

	@Override
	public void create () {
		batch = new SpriteBatch();
		backgroundImage = new Texture("road.png");
		Gdx.input.setInputProcessor(this);
		spawnTrafficLights();
		shapeRenderer = new ShapeRenderer();
		trafficLightManager = new TrafficLightManager(trafficLights);
		carsSpawnManager = new CarsSpawnManager();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		batch.begin();
		batch.draw(backgroundImage, 0, 0);
		batch.end();

		batch.begin();
		for(TrafficParticipant p : trafficParticipants){
			p.move();
			p.getSprite().draw(batch);
		}
		batch.end();

		trafficLightManager.updateLights();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		paintLights(shapeRenderer);
		shapeRenderer.end();

		batch.begin();
		for(TrafficLight tL : trafficLights){
			tL.getSprite().draw(batch);
		}
		batch.end();

	}


	
	@Override
	public void dispose () {
		batch.dispose();
		backgroundImage.dispose();
	}


	@Override
	public boolean keyDown(int keycode){
		if(keycode == Keys.LEFT){
			try {
				spawnCarsLeft();
			}catch (SpawnException e){
				System.out.println(e + " " + e.getDirection() + ", " + e.getLane() + " juostoje.");
			}
			catch (TrafficSimulationException e){
				System.out.println("" + e);
				keyDown(Keys.LEFT);
			}catch (CloneNotSupportedException e){
				e.printStackTrace();
			}

		}
		if(keycode == Keys.UP){
			try {
				spawnCarsUp();
			}catch (SpawnException e){
				System.out.println(e + " " + e.getDirection() + ", " + e.getLane() + " juostoje.");
			}catch (CloneNotSupportedException e){
				e.printStackTrace();
			}
		}
		if(keycode == Keys.RIGHT){
			try {
				spawnCarsRight();
			}catch (SpawnException e){
				System.out.println(e + " " + e.getDirection() + ", " + e.getLane() + " juostoje.");
			}catch (CloneNotSupportedException e){
				e.printStackTrace();
			}
		}
		if(keycode == Keys.DOWN){
			try {
				spawnCarsDown();
			}catch (SpawnException e){
				System.out.println(e + " " + e.getDirection() + ", " + e.getLane() + " juostoje.");
			}catch (CloneNotSupportedException e){
				e.printStackTrace();
			}
		}
		if(keycode == Keys.SPACE){
			cars.get(2).setCarDirection(Car.CarDirection.RIGHT_UP);
			cars.get(2).setCommand(Car.Command.TURN_LEFT);
			cars.get(2).setCarState(Car.CarState.TURNING);
		}
		if(keycode == Keys.A){
			try {
				spawnPedestrianLeft();
			}catch (CloneNotSupportedException e){
				e.printStackTrace();
			}
		}
		if(keycode == Keys.D){
			try {
				spawnPedestrianRight();
			}catch (CloneNotSupportedException e){
				e.printStackTrace();
			}
		}
		if(keycode == Keys.W){
			try {
				spawnPedestrianUp();
			}catch (CloneNotSupportedException e){
				e.printStackTrace();
			}
		}
		if(keycode == Keys.S){
			try {
				spawnPedestrianDown();
			}catch (CloneNotSupportedException e){
				e.printStackTrace();
			}
		}
		if(keycode == Keys.P){	//saves
			data.setData(this.trafficLightManager, this.carsSpawnManager, this.cars, this.pedestrians, this.trafficParticipants);
			WritingThread writingThread = new WritingThread(data);
			writingThread.start();
		}
		if(keycode == Keys.L){	//loads
			ReadingThread readingThread = new ReadingThread();
			readingThread.start();
			try {
				readingThread.join();
			}catch (InterruptedException e){
				e.printStackTrace();
			}

			SaveData data = readingThread.getData();
			carsSpawnManager = data.getCarsSpawnManager();
			cars = data.getCars();
			pedestrians = data.getPedestrians();
			trafficParticipants = data.getTrafficParticipants();
			for(TrafficParticipant participant : trafficParticipants){
				participant.updateSprite();
			}
			trafficLightManager = new TrafficLightManager(trafficLights);
			trafficLightManager.setCounter(data.getTrafficLightManager().getCounter());
			trafficLightManager.setNextLightCounter(data.getTrafficLightManager().getNextLightCounter());
			trafficLightManager.setState(data.getTrafficLightManager().getState());


		}
		return false;
	}


	private void paintLights(ShapeRenderer shapeRenderer){
		TrafficLight tL = trafficLights.get(0);
		shapeRenderer.setColor(tL.getStraightRightColor());
		shapeRenderer.rect(tL.getSprite().getX() + 22, tL.getSprite().getY() - 18, 24, 45);
		shapeRenderer.setColor(tL.getLeftColor());
		shapeRenderer.rect(tL.getSprite().getX() + 22, tL.getSprite().getY() + 28, 24, 20);

		tL = trafficLights.get(1);
		shapeRenderer.setColor(tL.getStraightRightColor());
		shapeRenderer.rect(tL.getSprite().getX(), tL.getSprite().getY() + 2, 45, 24);
		shapeRenderer.setColor(tL.getLeftColor());
		shapeRenderer.rect(tL.getSprite().getX() + 44, tL.getSprite().getY() + 5, 24, 20);

		tL = trafficLights.get(2);
		shapeRenderer.setColor(tL.getStraightRightColor());
		shapeRenderer.rect(tL.getSprite().getX() + 22, tL.getSprite().getY() + 2, 24, 45);
		shapeRenderer.setColor(tL.getLeftColor());
		shapeRenderer.rect(tL.getSprite().getX() + 22, tL.getSprite().getY() - 17, 24, 20);

		tL = trafficLights.get(3);
		shapeRenderer.setColor(tL.getStraightRightColor());
		shapeRenderer.rect(tL.getSprite().getX() + 24, tL.getSprite().getY() + 2, 45, 24);
		shapeRenderer.setColor(tL.getLeftColor());
		shapeRenderer.rect(tL.getSprite().getX(), tL.getSprite().getY() + 5, 24, 20);

	}

	private void spawnTrafficLights()
	{
		TrafficLight trafficLight;

		trafficLight = new TrafficLight();
		trafficLights.add(trafficLight);
		trafficLight.getSprite().rotate(-90f);
		trafficLight.getSprite().setPosition(200, 270);

		trafficLight = new TrafficLight();
		trafficLights.add(trafficLight);
		trafficLight.getSprite().rotate(180);
		trafficLight.getSprite().setPosition(250, 750);

		trafficLight = new TrafficLight();
		trafficLights.add(trafficLight);
		trafficLight.getSprite().rotate(90);
		trafficLight.getSprite().setPosition(730, 700);

		trafficLight = new TrafficLight();
		trafficLights.add(trafficLight);
		trafficLight.getSprite().setPosition(680, 220);
	}

	private void spawnPedestrianLeft() throws CloneNotSupportedException{
		Pedestrian pedestrian = new Pedestrian("ped1.png", -1, 90, new Vector2(250, 1100), Pedestrian.PedestrianState.MOVE_Y, Pedestrian.PedestrianCommand.GO);
		if(getRandomBetween2()) {
			pedestrian = (Pedestrian) pedestrian.clone();
			pedestrian.setVelocity(1);
			pedestrian.setSprite("ped.png", new Vector2(250, -100));
		}

		trafficParticipants.add(pedestrian);
		pedestrians.add(pedestrian);
	}

	private void spawnPedestrianRight() throws CloneNotSupportedException {
		Pedestrian pedestrian = new Pedestrian("ped.png", -1, 270, new Vector2(700, 1100), Pedestrian.PedestrianState.MOVE_Y, Pedestrian.PedestrianCommand.GO);
		if (getRandomBetween2()){
			pedestrian = (Pedestrian) pedestrian.clone();
			pedestrian.setVelocity(1);
			pedestrian.setSprite("ped1.png", new Vector2(700, -100));
		}

		trafficParticipants.add(pedestrian);
		pedestrians.add(pedestrian);
	}

	private void spawnPedestrianUp() throws CloneNotSupportedException {
		Pedestrian pedestrian = new Pedestrian("ped1.png", -1, 0, new Vector2(1100, 700), Pedestrian.PedestrianState.MOVE_X, Pedestrian.PedestrianCommand.GO);
		if (getRandomBetween2()){
			pedestrian = (Pedestrian) pedestrian.clone();
			pedestrian.setVelocity(1);
			pedestrian.setSprite("ped.png", new Vector2(-100, 700));
		}

		trafficParticipants.add(pedestrian);
		pedestrians.add(pedestrian);
	}

	private void spawnPedestrianDown() throws CloneNotSupportedException {
		Pedestrian pedestrian = new Pedestrian("ped1.png", -1, 0, new Vector2(1100, 265), Pedestrian.PedestrianState.MOVE_X, Pedestrian.PedestrianCommand.GO);
		if (getRandomBetween2()){
			pedestrian = (Pedestrian) pedestrian.clone();
			pedestrian.setVelocity(1);
			pedestrian.setSprite("ped.png", new Vector2(-100, 265));
		}

		trafficParticipants.add(pedestrian);
		pedestrians.add(pedestrian);
	}



	public void spawnCarsLeft () throws TrafficSimulationException, CloneNotSupportedException{
		Car car;
		float randPos = -100-300*getRandom();
		Car lastCar = carsSpawnManager.getLeftLast(0);
		if(lastCar != null && randPos + 120 > lastCar.getSprite().getX()) {
			throw new SpawnException("kairėje", 1);
		}
		car = new Car(getRandomCarImage(), 0, 0, new Vector2(randPos, 445), Car.CarDirection.RIGHT, Car.CarState.MOVE_X, lastCar);
		trafficParticipants.add(car);
		cars.add(car);
		carsSpawnManager.setLeftLast(0, car);
		//-----------------KITA JUOSTA-------------------
		randPos = -100-300*getRandom();
		lastCar = carsSpawnManager.getLeftLast(1);
		if(lastCar != null && randPos + 120 > lastCar.getSprite().getX()) {
			throw new SpawnException("kairėje", 2);
		}
		car = (Car) car.clone();
		car.setSprite(getRandomCarImage(), new Vector2(randPos, 385));
		car.setAhead(lastCar);
		trafficParticipants.add(car);
		cars.add(car);
		carsSpawnManager.setLeftLast(1, car);
		//-----------------KITA JUOSTA-------------------
		randPos = -100-300*getRandom();
		lastCar = carsSpawnManager.getLeftLast(2);
		if(lastCar != null && randPos + 120 > lastCar.getSprite().getX()) {
			throw new SpawnException("kairėje", 3);
		}
		car = (Car) car.clone();
		car.setSprite(getRandomCarImage(), new Vector2(randPos, 325));
		car.setAhead(lastCar);
		trafficParticipants.add(car);
		cars.add(car);
		carsSpawnManager.setLeftLast(2, car);
	}
	public void spawnCarsUp () throws SpawnException, CloneNotSupportedException{
		Car car;
		float randPos = 1100+300*getRandom();
		Car lastCar = carsSpawnManager.getUpLast(0);
		if(lastCar != null && randPos - 120 < lastCar.getSprite().getY()) {
			throw new SpawnException("viršuje" , 1);
		}
		car = new Car(getRandomCarImage(), 0, 270, new Vector2(425, randPos), Car.CarDirection.DOWN, Car.CarState.MOVE_Y, lastCar);
		trafficParticipants.add(car);
		cars.add(car);
		carsSpawnManager.setUpLast(0, car);
		//-----------------KITA JUOSTA-------------------
		randPos = 1100+300*getRandom();
		lastCar = carsSpawnManager.getUpLast(1);
		if(lastCar != null && randPos - 120 < lastCar.getSprite().getY()) {
			throw new SpawnException("viršuje" , 2);
		}
		car = (Car) car.clone();
		car.setSprite(getRandomCarImage(), new Vector2(365, randPos));
		car.setAhead(lastCar);
		trafficParticipants.add(car);
		cars.add(car);
		carsSpawnManager.setUpLast(1, car);
		//-----------------KITA JUOSTA-------------------
		randPos = 1100+300*getRandom();
		lastCar = carsSpawnManager.getUpLast(2);
		if(lastCar != null && randPos - 120 < lastCar.getSprite().getY()) {
			throw new SpawnException("viršuje" , 3);
		}
		car = (Car) car.clone();
		car.setSprite(getRandomCarImage(), new Vector2(305, randPos));
		car.setAhead(lastCar);
		trafficParticipants.add(car);
		cars.add(car);
		carsSpawnManager.setUpLast(2, car);
	}
	public void spawnCarsRight () throws SpawnException, CloneNotSupportedException{
		Car car;
		float randPos = 1100+300*getRandom();
		Car lastCar = carsSpawnManager.getRightLast(0);
		if(lastCar != null && randPos - 120 < lastCar.getSprite().getX()) {
			throw new SpawnException("dešinėje" , 1);
		}
		car = new Car(getRandomCarImage(), 0, 180, new Vector2(randPos, 510), Car.CarDirection.LEFT, Car.CarState.MOVE_X, lastCar);
		trafficParticipants.add(car);
		cars.add(car);
		carsSpawnManager.setRightLast(0, car);
		//-----------------KITA JUOSTA-------------------
		randPos = 1100+300*getRandom();
		lastCar = carsSpawnManager.getRightLast(1);
		if(lastCar != null && randPos - 120 < lastCar.getSprite().getX()) {
			throw new SpawnException("dešinėje" , 2);
		}
		car = (Car) car.clone();
		car.setSprite(getRandomCarImage(), new Vector2(randPos, 570));
		car.setAhead(lastCar);
		trafficParticipants.add(car);
		cars.add(car);
		carsSpawnManager.setRightLast(1, car);
		//-----------------KITA JUOSTA-------------------
		randPos = 1100+300*getRandom();
		lastCar = carsSpawnManager.getRightLast(2);
		if(lastCar != null && randPos - 120 < lastCar.getSprite().getX()) {
			throw new SpawnException("dešinėje" , 3);
		}
		car = (Car) car.clone();
		car.setSprite(getRandomCarImage(), new Vector2(randPos, 630));
		car.setAhead(lastCar);
		trafficParticipants.add(car);
		cars.add(car);
		carsSpawnManager.setRightLast(2, car);
	}
	public void spawnCarsDown () throws SpawnException, CloneNotSupportedException{
		Car car;
		float randPos = -100-300*getRandom();
		Car lastCar = carsSpawnManager.getDownLast(0);
		if(lastCar != null && randPos + 120 > lastCar.getSprite().getY()) {
			throw new SpawnException("apačioje" , 1);
		}
		car = new Car(getRandomCarImage(), 0, 90, new Vector2(485, randPos), Car.CarDirection.UP, Car.CarState.MOVE_Y, lastCar);
		trafficParticipants.add(car);
		cars.add(car);
		carsSpawnManager.setDownLast(0, car);
		//-----------------KITA JUOSTA-------------------
		randPos = -100-300*getRandom();
		lastCar = carsSpawnManager.getDownLast(1);
		if(lastCar != null && randPos + 120 > lastCar.getSprite().getY()) {
			throw new SpawnException("apačioje" , 2);
		}
		car = (Car) car.clone();
		car.setSprite(getRandomCarImage(), new Vector2(545, randPos));
		car.setAhead(lastCar);
		trafficParticipants.add(car);
		cars.add(car);
		carsSpawnManager.setDownLast(1, car);
		//-----------------KITA JUOSTA-------------------
		randPos = -100-300*getRandom();
		lastCar = carsSpawnManager.getDownLast(2);
		if(lastCar != null && randPos + 120 > lastCar.getSprite().getY()) {
			throw new SpawnException("apačioje" , 3);
		}
		car = (Car) car.clone();
		car.setSprite(getRandomCarImage(), new Vector2(605, randPos));
		car.setAhead(lastCar);
		trafficParticipants.add(car);
		cars.add(car);
		carsSpawnManager.setDownLast(2, car);
	}


	private static float getRandom() {
		return (float)Math.random();
	}
	private static boolean getRandomBetween2(){
		return Math.random() > 0.5;
	}
	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	private String getRandomCarImage()
	{
		int tempNumber = getRandomNumberInRange(1, 5);
		return ("car" + tempNumber + ".png");
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}

