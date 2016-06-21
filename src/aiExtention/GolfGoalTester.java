package aiExtention;

import com.badlogic.gdx.math.Vector3;
import physics.components.Position;
import searchTree.GoalAchived;

public class GolfGoalTester extends GoalAchived<GolfState> {
	private final int diameterOfTarget = 10;

	@Override
	public boolean test(GolfState aState) {
		Vector3 difference = aState.getDistanceToTarget();
		return difference.len() < diameterOfTarget;
	}

}
