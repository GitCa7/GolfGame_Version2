package aiExtention;

import com.badlogic.gdx.math.Vector3;
import searchTree.AbstractAction;

public class GolfAction extends AbstractAction<GolfState> {
	private Vector3 force;

	public GolfAction(Vector3 force) {
		this.force = force;
	}

	public Vector3 getForce() {
		return force;
	}
}
