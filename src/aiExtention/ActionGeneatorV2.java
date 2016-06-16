package aiExtention;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class ActionGeneatorV2 {

	private final int maxForceLength = 10;

	private Vector3 firstTryDir;

	private int maxNumberOfRep;

	private int currentRep = 0;

	private int strengthItteration = 3;
	private int zAxisItterations = 3;

	private ArrayList<Vector3> forceData = new ArrayList<Vector3>();


	public ActionGeneatorV2(int maxNumOfRep) {
		this.maxNumberOfRep = maxNumOfRep;
		generateForceData();

	}

	public Vector3 generateNextForce() {
		if (currentRep == maxNumberOfRep) {
			resetCount();
		}
		currentRep++;
		return forceData.get(currentRep - 1);

	}

	public void generateForceData() {
		int denominator = strengthItteration * zAxisItterations;
		System.out.println(denominator);
		int planXYnumber = ((maxNumberOfRep) / denominator) + 1;
		System.out.println(planXYnumber);
		Vector3 zAxis = new Vector3(0, 0, 1);
		ArrayList<Vector3> verticalPermutations = generateVerticalIncrement(-30, 3);
		for (int i = 0; i < verticalPermutations.size(); i++) {

			Vector3 referenceVector = verticalPermutations.get(i).cpy();

			for (int j = 0; j < planXYnumber; j++) {
				referenceVector.rotate(zAxis, 360 / planXYnumber);
				generateForceIncrement(referenceVector);

			}
		}
	}

	public void setReferanceVector(Vector3 referanceVector) {
		Vector3 copy = referanceVector.cpy();
		copy.scl(copy.len());
		this.firstTryDir = copy;
	}

	public ArrayList<Vector3> generateVerticalIncrement(int degrees, int noRays) {
		Vector3 yAxis = new Vector3(0, 1, 0);
		ArrayList<Vector3> verticalPermutations = new ArrayList<Vector3>();
		for (int i = 0; i < noRays; i++) {
			Vector3 copy = new Vector3(1, 0, 0);
			verticalPermutations.add(copy.cpy().rotate(yAxis, degrees * i));

		}
		return verticalPermutations;
	}

	public void generateForceIncrement(Vector3 vector) {
		for (int i = 0; i < strengthItteration; i++)
			forceData.add(vector.cpy().scl(maxForceLength * (strengthItteration - i) / strengthItteration));

	}

	public ArrayList<Vector3> getForceData() {
		return forceData;
	}

	public void setForceData(ArrayList<Vector3> forceData) {
		this.forceData = forceData;
	}

	public void resetCount() {
		currentRep = 0;
	}

}
