package entities;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import terrrains.Terrain;

public class Player extends Entity {
	
	private static final float RUN_SPEED=20;//per second
	private static final float TURN_SPEED=160;//per second
	public static final float GRAVITY=-50;//per second
	private static final float JUMP_POWER=30;//per second
	
	private static final float TERRAIN_HEIGHT=0;
	
	private float currentSpeed=0;
	private float currentTurnSpeed=0;
	private float upwardsSpeed=0;
	
	private boolean isInAir=false;
	
	public Player(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		
		super(model, position, rotX, rotY, rotZ, scale);

	}
	
	public void move(Terrain terrain){
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeInSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeInSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed+=GRAVITY*DisplayManager.getFrameTimeInSeconds();
		super.increasePosition(0, upwardsSpeed*DisplayManager.getFrameTimeInSeconds(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y<terrainHeight){
			upwardsSpeed=0;
			super.getPosition().y=terrainHeight;
			isInAir=false;
		}
	}
	
	private void jump(){
		if(!isInAir){
			this.upwardsSpeed=JUMP_POWER;
			isInAir=true;
		}
	}
	
	private void checkInputs(){
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			this.currentSpeed=RUN_SPEED;

		}else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			this.currentSpeed=-RUN_SPEED;
		}else{
			this.currentSpeed=0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			this.currentTurnSpeed=-TURN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			this.currentTurnSpeed=TURN_SPEED;
		}else{
			this.currentTurnSpeed=0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			jump();
		}
		
	}
	
}
