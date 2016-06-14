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
        System.out.println(toPlay.size());
        for (int i=0;i<toPlay.size();i++){
            System.out.println(name);
            System.out.println(toPlay.get(i).getName());
            if (name.equals(toPlay.get(i).getName())){
                System.out.println("Y");
                play = toPlay.get(i);
            }else{System.out.println("N");}
        }
        return play;
    }

}
