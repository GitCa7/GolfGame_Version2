package testForAi;

import aiExtention.ActionGeneatorV2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class RotTest {

	public static void main(String[] args) {
		ActionGeneatorV2 generator = new ActionGeneatorV2(1000);
		ArrayList<Vector3> myList = generator.getForceData();
		System.out.println(myList.size());
		for (int i = 0; i < myList.size(); i++) {
			System.out.println(myList.get(i));
			System.out.println(myList.get(i).len());

		}
	}

}
