package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float distanceFromPlayer=50;
	private float angleAroundPlayer=0;
	
	private Vector3f position = new Vector3f(100,1,-30);
	private float  pitch=20;//high or low
	private float yaw;//left or right
	private float roll;//how much tilted
	
	private Player player;
	
	public Camera(Player player){
		this.player = player;
	}
	
	public void move(){
		calculateAngleAroundPlayer();
		calculatePitch();
		calculateZoom();
		float horizontalDistance = calculateHorizontalDistance();
		float verticallDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance,verticallDistance);
		this.yaw = 180-(player.getRotY() + angleAroundPlayer);
	}

	
	
	private void calculateCameraPosition(float horizDistance,float verticDistance){
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x=player.getPosition().x-offsetX;
		position.z=player.getPosition().z-offsetZ;
		position.y = player.getPosition().y+verticDistance;
		
	}
	
	private float calculateHorizontalDistance(){
		return distanceFromPlayer * (float) Math.cos(Math.toRadians(pitch));
	}
	private float calculateVerticalDistance(){
		return distanceFromPlayer * (float) Math.sin(Math.toRadians(pitch));
	}
	
	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel()*0.1f;
		distanceFromPlayer -=zoomLevel;
		if(distanceFromPlayer<=10)
			distanceFromPlayer=10;
		if(distanceFromPlayer>=200)
			distanceFromPlayer=200;
	}
	
	private void calculatePitch(){
		if(Mouse.isButtonDown(1)){
			float pitchChange = Mouse.getDY()*0.1f;
			pitch-=pitchChange;
			if(pitch<=10)
				pitch=10;
			if(pitch>=80)
				pitch=80;
			
			
		}
	}
	
	private void calculateAngleAroundPlayer(){
		if(Mouse.isButtonDown(0)){
			float angleChange =Mouse.getDX()*0.3f;
			angleAroundPlayer-=angleChange;
		}
	}
	
	//Getters
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
}
