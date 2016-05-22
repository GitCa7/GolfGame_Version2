package RenderComponents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import Entities.Camera;
import Entities.Light;
import Entities.gameEntity;
import ModelBuildComponents.TexturedModel;
import ShaderPackage.StaticShader;
import ShaderPackage.terrainShader;
import SkyboxComponents.SkyboxRenderer;
import TerrainComponents.Terrain;



public class MasterRenderer {
    
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 5000;
     
    private Matrix4f projectionMatrix;
     
    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;
     
    private TerrainRenderer terrainRenderer;
    private terrainShader terraShader = new terrainShader();
     
     
    private Map<TexturedModel,List<gameEntity>> entities = new HashMap<TexturedModel,List<gameEntity>>();
    private List<Terrain> terrains = new ArrayList<Terrain>();
     
    private SkyboxRenderer skyboxRenderer;
    
    public MasterRenderer(Loader loader){
        enableCulling();
        createProjectionMatrix();
        renderer = new EntityRenderer(shader,projectionMatrix);
        terrainRenderer = new TerrainRenderer(terraShader,projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
    }
    
    
    public static void enableCulling()	{
    	GL11.glEnable(GL11.GL_CULL_FACE);
    	GL11.glCullFace(GL11.GL_BACK);
    }
    
    public static void disableCulling()	{
    	GL11.glDisable(GL11.GL_CULL_FACE);
    }
    
    public Matrix4f getProjectionMatrix()	{
    	return projectionMatrix;
    }
     
    public void render(Light sun,Camera camera){
    	
        prepare();
        
        //starts the Shader for Entities
        shader.start();
        
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);
        
        //Entites are rendered
        renderer.render(entities);
        
        shader.stop();
        
      //starts the Shader for Terrain
        terraShader.start();
        terraShader.loadLight(sun);
        terraShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terraShader.stop();
        skyboxRenderer.render(camera);
        
        
        terrains.clear();
        entities.clear();
    }
     
    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }
     
    public void processEntity(gameEntity gameEntity){
        TexturedModel gameEntityModel = gameEntity.getModel();
        List<gameEntity> batch = entities.get(gameEntityModel);
        if(batch!=null){
            batch.add(gameEntity);
        }else{
            List<gameEntity> newBatch = new ArrayList<gameEntity>();
            newBatch.add(gameEntity);
            entities.put(gameEntityModel, newBatch);        
        }
    }
     
    public void cleanUp(){
        shader.cleanUp();
        terraShader.cleanUp();
    }
     
    public void prepare() {
    	//System.out.println("Entities Size: " + physics.entities.size());
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.0f, 0f, 0.0f, 1);
    }
     
    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
 
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }
     
 
}