package RenderComponents;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import Entities.gameEntity;
import LogicAndExtras.Maths;
import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;
import ShaderPackage.StaticShader;



public class EntityRenderer {
	 
    private StaticShader shader;
 
    public EntityRenderer(StaticShader shader,Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
 
    public void render(Map<TexturedModel, List<gameEntity>> entities) {
        for (TexturedModel model : entities.keySet()) {
            preparetexturedModel(model);
            List<gameEntity> batch = entities.get(model);
            for (gameEntity gameEntity : batch) {
                prepareInstance(gameEntity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }
 
    private void preparetexturedModel(TexturedModel model) {
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        ModelTexture texture = model.getTexture();
        
        if(texture.getHasTranspercy())	{
        	MasterRenderer.disableCulling();
        }
        shader.loadFakeLightingVariable(texture.isUseFakeLighting());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
    }
 
    private void unbindTexturedModel() {
    	MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
 
    private void prepareInstance(gameEntity gameEntity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(gameEntity.getPosition(),
                gameEntity.getRotX(), gameEntity.getRotY(), gameEntity.getRotZ(), gameEntity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }
 
}