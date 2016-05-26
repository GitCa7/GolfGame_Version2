package Editor;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Asus on 26-5-2016.
 */
public class CourseLoader {

    public static Course loadCourse(String name){
        ObjectInputStream inputStream;
        ArrayList<Course> toPlay=null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream("courses.dat"));
            toPlay = (ArrayList<Course>) inputStream.readObject();
        } catch (IOException ex) {
            System.out.print(ex.toString());
        } catch (ClassNotFoundException ex) {
            System.out.print(ex.toString());
        }
        Course play=null;
        for (int i=0;i<toPlay.size();i++){
            if (name.equals(toPlay.get(i).getName())){
                play = toPlay.get(i);
            }
        }
        return play;
    }

}
