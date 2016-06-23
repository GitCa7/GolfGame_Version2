package aiExtention.Utils;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

public class ActionGeneratorV3 {
	private final int maxForceLength = 20;

	private Vector3 firstTryDir;

	private int maxNumberOfRep;

	private int currentRep = 0;

	private int strengthItteration = 1;
	private int zAxisItterations = 1;
	
	private final int sightDegrees = 60;

	private ArrayList<Vector3> forceData = new ArrayList<Vector3>();


	public ActionGeneratorV3(Vector3 directionBias, int noOfNodes) {
		firstTryDir= directionBias;
		maxNumberOfRep=noOfNodes;
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
		
		int planXYnumber = ((maxNumberOfRep) / denominator) + 1;
		
		Vector3 zAxis = new Vector3(0, 0, 1);
		

			Vector3 referenceVector = new Vector3(firstTryDir.x,firstTryDir.y,0);
			referenceVector.scl(referenceVector.len());
			
			referenceVector.rotate(zAxis, -sightDegrees/2);

			for (int j = 0; j < planXYnumber; j++) {
				referenceVector.rotate(zAxis,  sightDegrees/(2*planXYnumber));
				generateForceIncrement(referenceVector);

			}
			referenceVector = new Vector3(0,firstTryDir.y,firstTryDir.z);
			referenceVector.scl(referenceVector.len());
			
//			for (int j = 0; j < planXYnumber; j++) {
//				referenceVector.rotate(zAxis,  sightDegrees/(2*planXYnumber));
//				generateForceIncrement(referenceVector);
//
//			}
			
		
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
	
	





