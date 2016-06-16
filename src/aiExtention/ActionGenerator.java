package aiExtention;


import com.badlogic.gdx.math.Vector3;

public class ActionGenerator {

	// private final int maxForceValue = 14;
	private final int maxXYcoordinate = 20;

	public Vector3 generateRandomForce() {

		float a = (float) (maxXYcoordinate * Math.sin(Math.random() * 2 * Math.PI));
		float b = (float) (maxXYcoordinate * Math.sin(Math.random() * 2 * Math.PI));
		float c = (float) (maxXYcoordinate * Math.sin(Math.random() * 2 * Math.PI));
		return new Vector3(a, b, c);

	}

}
