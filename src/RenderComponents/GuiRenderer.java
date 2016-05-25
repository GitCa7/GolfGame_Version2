package RenderComponents;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


import GUIs.GUITexture;
import GUIs.GuiShader;
import ModelBuildComponents.RawModel;

public class GuiRenderer {
	
	private final RawModel model;
	private GuiShader shader;
	
	public GuiRenderer(Loader loader)	{
		float[] position = {-1, 1, -1, -1, 1, 1, 1, -1};
		model = loader.loadToVAO(position, 2);
		shader = new GuiShader();
	}
	
	public void render(List<GUITexture> guis)	{
		shader.start();
		GL30.glBindVertexArray(model.getID());
		GL20.glEnableVertexAttribArray(0);
		
		for(GUITexture gui: guis)	{
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, model.getVertexCount());
		}
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	public void cleanUp()	{
		shader.cleanUp();
	}

}
