package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import particles.Particle;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolBox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;

public class MainGameLoop {
	
	private static TerrainTexturePack terrainTexturePack;

	public static void main(String[] args){
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		Random random = new Random();
		
		//**********************MODELS*************//
		RawModel model1 = OBJLoader.loadObjModel("tree", loader);
		ModelTexture texture1 = new ModelTexture(loader.loadTexture("tree"));
		TexturedModel texturedModel1 = new TexturedModel(model1, texture1);
		
		RawModel model2 = OBJLoader.loadObjModel("grassModel", loader);
		ModelTexture texture2 = new ModelTexture(loader.loadTexture("grassTexture"));
		texture2.setHasTransparency(true);
		texture2.setUseFakeLighting(true);
		TexturedModel texturedModel2 = new TexturedModel(model2, texture2);
		
		RawModel model3 = OBJLoader.loadObjModel("fern", loader);
		ModelTexture texture3 = new ModelTexture(loader.loadTexture("ferns"));
		texture3.setHasTransparency(true);
		texture3.setNumberOfRows(2);
		TexturedModel texturedModel3 = new TexturedModel(model3, texture3);
		
		RawModel model4 = OBJLoader.loadObjModel("person", loader);
		ModelTexture texture4 = new ModelTexture(loader.loadTexture("playerTexture"));
		TexturedModel texturedModel4 = new TexturedModel(model4, texture4);
		
		RawModel lampModel = OBJLoader.loadObjModel("lamp", loader);
		ModelTexture lampTexture = new ModelTexture(loader.loadTexture("lamp"));
		lampTexture.setShineDamper(10);
		TexturedModel lamp = new TexturedModel(lampModel, lampTexture);
		
		//**************************************************//
		
		//**********TERRAIN TEXTURE STUFF*************//
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack terrainTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		Terrain terrain1 = new Terrain(0,-1,loader,terrainTexturePack,blendMap,"heightmap");
		List<Terrain > terrains = new ArrayList<Terrain>();
		terrains.add(terrain1);
		//**************ENTITIES
		
		List<Entity> entities = new ArrayList<Entity>();

		Player player = new Player(texturedModel4,new Vector3f(180,0,-250),0,0,0,1);
		Camera camera = new Camera(player);
		Light light = new Light(new Vector3f(185,3f,-293),new Vector3f(0,2,2),new Vector3f(1f,0.01f,0.002f));
		Entity lampEntity = new Entity(lamp, new Vector3f(185,-4.7f,-293), 0, 0, 0, 1);
		for(int i=0;i<100;i++){
			float x = random.nextFloat()*800;
			float z = random.nextFloat()*-800;
			float y = terrain1.getHeightOfTerrain(x, z);
			entities.add(new Entity(texturedModel2,new Vector3f(x,y,z),0,0,0,3));
			x = random.nextFloat()*800;
			z = random.nextFloat()*-800;
			y = terrain1.getHeightOfTerrain(x, z);
			entities.add(new Entity(texturedModel1,new Vector3f(x,y,z),0,0,0,8));
			x = random.nextFloat()*800;
			z = random.nextFloat()*-800;

			y = terrain1.getHeightOfTerrain(x, z);
			entities.add(new Entity(texturedModel3,random.nextInt(4),new Vector3f(x,y,z),0,0,0,2));
			

		}
		entities.add(lampEntity);
		entities.add(player);
		
		MasterRenderer renderer = new MasterRenderer(loader);
		MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix(),terrain1);

		//************************PARTICLES************************//
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particleAtlas"), 4,false);
		ParticleSystem system = new ParticleSystem(particleTexture,50, 25, 0.2f, 2);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
		//**********************WATER*******
		
//		WaterShader waterShader = new WaterShader();
//		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix());
//		List<WaterTile> waters = new ArrayList<WaterTile>();
//		waters.add(new WaterTile(405, -445, 0));		
//		
//		WaterFrameBuffers fbos = new WaterFrameBuffers();
	
		//*********************LOOP*********************
		long lastTime = System.nanoTime(),now=0,timer=0;
		int frames=0;
		while(!Display.isCloseRequested()){
			
				//***************UPDATE****************
				camera.move();
				
				player.move(terrain1);
				
				picker.update();
				
				
				Vector3f terrainPoint = picker.getCurrentTerrainPoint();
				if(terrainPoint!=null && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
					lampEntity.setPosition(terrainPoint);
					light.setPosition(new Vector3f(terrainPoint.x,terrainPoint.y+15,terrainPoint.z));
				}
				if(terrainPoint!=null && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
					system.generateParticles(terrainPoint);
				}
				
				
				ParticleMaster.update(camera);
				
				//****************RENDER***********
			
				//IN FBO
				///fbos.bindReflectionFrameBuffer();
			//	renderer.renderScene(entities, terrains, light, camera);
			//	fbos.unbindCurrentFrameBuffer();
				
				//NORMAL
				renderer.renderScene(entities, terrains, light, camera);
				ParticleMaster.renderParticle(camera);
			//	waterRenderer.render(waters, camera);
				
			DisplayManager.updateDisplay();
			now=System.nanoTime();
			timer+=now-lastTime;
			lastTime=now;
			frames++;
			if(timer>=1000000000){
				Display.setTitle("3D Game Engine  FPS: " + frames);
				timer=0;
				frames=0;
			}
		}
	//	fbos.cleanUp();
	//	waterShader.cleanUp();
		ParticleMaster.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}
	
	
	
	
}
