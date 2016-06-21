package Editor;

import Entities.*;
import LogicAndExtras.MousePicker;
import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;
import RenderComponents.Loader;
import RenderComponents.MasterRenderer;
import RenderComponents.OBJLoader;
import TerrainComponents.Terrain;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import org.lwjgl.util.vector.Vector3f;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Editor implements ApplicationListener {
    MListener mouse;
    Loader loader;
    Camera camera;
    MasterRenderer renderer;
    RawModel model;
    TexturedModel staticModel;
    Light light;
    MousePicker picker;
    ArrayList<gameEntity> renderEntities = new ArrayList();
    Terrain terrain;
    private float size;
    private final boolean first = false;
    String sizeInput;

    public Editor(){
        sizeInput = JOptionPane.showInputDialog("Terrain Size?");
    }

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new Editor(), config);
    }

    @Override
    public void create() {
        loader = new Loader();

        size = (float) Integer.parseInt(sizeInput);
        terrain = new Terrain( size);
        camera = new Camera(new Vector3f(-size / 2, size / 2, size / 2));
        renderer = new MasterRenderer(loader);
        picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
        mouse = new MListener(loader, camera, terrain, picker, this);
        Gdx.input.setInputProcessor(mouse);

        terrain.changeHeightNB(20, -size - 20, 20, -20, 80);
        terrain.changeHeightNB(20, -size - 20, -size + 20, -size - 20, 80);
        terrain.changeHeightNB(20, -20, 20, -size - 20, 80);
        terrain.changeHeightNB(-size + 20, -size - 20, 20, -size - 20, 80);

        /*model = OBJLoader.loadObjModel("dragon", loader);

        staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("sand")));

        gameEntity dragon = new gameEntity(staticModel, new Vector3f(-50, 0, -50), 0, 0, 0, 5);
        dragon.setRotY(-40);
        renderEntities.add(dragon);*/

        if (first) {
            ObjectOutputStream outputStream = null;
            try {
                Course tmp = new Course(null,null,null,null,null,"null");
                ArrayList<Course> courses = new ArrayList<Course>();
                courses.add(tmp);
                System.out.println("yt");
                outputStream = new ObjectOutputStream(new FileOutputStream("C:\\Users\\Asus\\Documents\\UNI\\GolfGame_Version2\\courses.dat"));
                outputStream.writeObject(courses);
            } catch (IOException ex) {
                System.out.println("[Laad] IO Error11: " + ex.getMessage());
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.flush();
                        outputStream.close();
                    }
                } catch (IOException e2) {
                    System.out.println("[Update] Error12: " + e2.getMessage());
                }
            }
        }


        RawModel grassModel = OBJLoader.loadObjModel("grassModel", loader);
        ModelTexture grassModelText = new ModelTexture(loader.loadTexture("grassTexture"));
        grassModelText.setHasTranspercy(true);
        grassModelText.setUseFakeLighting(true);

        RawModel fernModel = OBJLoader.loadObjModel("fern", loader);
        ModelTexture fernModelText = new ModelTexture(loader.loadTexture("fern"));
        fernModelText.setHasTranspercy(true);
        fernModelText.setUseFakeLighting(true);

        TexturedModel grassTextModel = new TexturedModel(grassModel, grassModelText);
        TexturedModel fernTextModel = new TexturedModel(fernModel, fernModelText);


        light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));


        //mouse.entities().add(new Entity())


        Random ran = new Random();
        float x, z;
        for (int i = 0; i < size / 9; i++) {
            x = ran.nextFloat() * (size - 50) - (size);
            z = ran.nextFloat() * -size;
            renderEntities.add(new gameEntity(grassTextModel, new Vector3f(x, mouse.terrain().getHeightSimple(x, z), z), 180, 0, 0, 3));

            x = ran.nextFloat() * (size - 50) - (size);
            z = ran.nextFloat() * -size;
            renderEntities.add(new gameEntity(fernTextModel, new Vector3f(x, mouse.terrain().getHeightSimple(x, z), z), 0, 0, 0, 3));
        }


        updateEntities();


        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resize(int i, int i1) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render() {
        camera.move();
        picker.update();
        //System.out.println(picker.getCurrentRay().x + " | " + picker.getCurrentRay().y + " | " + picker.getCurrentRay().z);
        //System.out.println(mousePick.getCurrentRay());

        renderer.processTerrain(mouse.terrain());

        for (gameEntity entity : renderEntities) {
            renderer.processEntity(entity);

        }
        for (gameEntity entity : mouse.entities()) {
            renderer.processEntity(entity);

        }
        for (gameEntity entity : mouse.balls()) {
            renderer.processEntity(entity);

        }
        renderer.processEntity(mouse.hole());

        renderer.render(light, camera);

    }

    public void updateEntities() {
        for (gameEntity entity : renderEntities) {
            if (entity.getPosition().y != mouse.terrain().getHeightDif(entity.getPosition().x, entity.getPosition().z)) {
                entity.setPosition(new Vector3f(entity.getPosition().x, mouse.terrain().getHeightDif(entity.getPosition().x, entity.getPosition().z), entity.getPosition().z));
            }
        }
        for (gameEntity entity : mouse.entities()) {
            if (entity.getPosition().y < mouse.terrain().getHeightDif(entity.getPosition().x, entity.getPosition().z)) {
                entity.setPosition(new Vector3f(entity.getPosition().x, mouse.terrain().getHeightDif(entity.getPosition().x, entity.getPosition().z), entity.getPosition().z));
            }
        }
    }

    @Override
    public void pause() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resume() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dispose() {
        renderer.cleanUp();
        loader.cleanUp();
        Gdx.gl.glFinish();
        Gdx.gl.glFlush();
        Gdx.gl20.glFinish();
        Gdx.gl20.glFlush();
        Gdx.app.exit();
    }

    public void save() {
        ArrayList<Vector3f> tmp = new ArrayList<>();
        ArrayList<Boolean> tmp2 = new ArrayList<>();
        for(GolfBall a:mouse.balls()){
            tmp.add(a.getPosition());
            tmp2.add(a.isBot());
        }
        ArrayList<ObstacleDat> obdat = new ArrayList<>();
        for(gameEntity a:mouse.entities()){
            obdat.add(a.toData());
        }
        terrain.printVerts();
        System.out.println("--------------------------------------------------------------------------------------");
        mouse.terrain().toData().printVerts();
        String name = JOptionPane.showInputDialog("Course Name?");
        Course toSave = new Course(mouse.terrain.toData(), obdat, tmp,tmp2, mouse.hole.getPosition(),name);
        System.out.println(toSave.getName()) ;
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream("courses.dat"));
            System.out.println("g");
            ArrayList<Course> courses = (ArrayList<Course>) inputStream.readObject();
            courses.add(toSave);
            outputStream = new ObjectOutputStream(new FileOutputStream("courses.dat"));
            outputStream.writeObject(courses);
        } catch (IOException ex) {
            System.out.println("[Laad] IO Error: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("error");
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                }
            } catch (IOException e2) {
                System.out.println("[Update] Error: " + e2.getMessage());
            }
        }

       // this.dispose();
    }
}

