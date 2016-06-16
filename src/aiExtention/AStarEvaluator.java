package aiExtention;

import com.badlogic.gdx.math.Vector3;
import physics.components.Position;
import searchTree.NodeEvaluator;
import searchTree.TreeNode;

public class AStarEvaluator  extends NodeEvaluator<GolfState> {
	private int maxDist= 20;

	@Override
	public double evaluateNode(TreeNode<GolfState, ?> aNode) {

		GolfState aState = aNode.getState();


		Vector3 difference = aState.getTarget().getComponent(Position.class).cpy()
				.sub(aState.getBall().mEntity.getComponent(Position.class).cpy());

		// for (int i = 0; i < aState.getCourt().getToxicAreasList().size(); i++) {
		// if (aState.getCourt().getToxicAreasList().get(i).getToxicArea()
		// .contains(aState.getBall().getBallPosition().x, aState.getBall().getBallPosition().y)) {
		// return Integer.MAX_VALUE;
		// }
		// }
		 

		return  difference.len()/maxDist+aNode.getNodeDeapth();
	}

}
