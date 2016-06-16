package aiExtention;

import com.badlogic.ashley.core.Engine;
import physics.components.Force;
import physics.components.Velocity;
import physics.entities.Ball;
import searchTree.NodeEvaluator;
import searchTree.NodeGenerator;
import searchTree.TreeNode;

public class GolfGenerator extends NodeGenerator<GolfState, GolfAction> {

	private Engine moveManager;
	private NodeEvaluator<GolfState> evaluator ;
	private ActionGeneatorV2 forceGenerator = new ActionGeneatorV2(1000);

	public GolfGenerator(Engine engine, NodeEvaluator<GolfState> evaluator) {
		super(evaluator);
		this.evaluator= evaluator;
		moveManager = engine;
		
	}

	@Override
	public TreeNode<GolfState, GolfAction> generateChildNode(TreeNode<GolfState, GolfAction> aNode) {

		TreeNode<GolfState, GolfAction> childNode = new TreeNode<GolfState, GolfAction>(aNode);
		GolfState childState = aNode.getState().cloneState();
		GolfAction action = new GolfAction(forceGenerator.generateNextForce());

		// System.out.println("Action " + action.getForce());
		applyAction(childState, action);
		childNode.setState(childState);
		childNode.setAction(action);
		childNode.setNodeDeapth(aNode.getNodeDeapth() + 1);
		childNode.setValueOfNode(evaluator.evaluateNode(childNode));
		// System.out.println(evaluator.evaluateNode(childNode));
		// aNode.addChildNode(childNode);
		// System.out.println("childNodeActonStore" + childNode.getAction().getForce());
		return childNode;
	}

	private void applyAction(GolfState childState, GolfAction anAction) {

		// moveManager.addEntity(childState.getBall());
		// System.out.println(childState.getBall());
		// Vector3 forceAplly = forceGenerator.generateRandomForce();
		// Ball ball = childState.getBall();
		// ball.getComponent(Force.class).add(forceAplly.x, forceAplly.y, forceAplly.z);
		// moveManager.update(1f);
		// moveManager.update(1f);
		// while (childState.getBall().getComponent(Velocity.class).len() > 0.1) {
		// moveManager.update(1f);
		// System.out.println("running engine");

		Engine engine = moveManager;
		engine.removeAllEntities();
		// System.out.println(engine.g);
		Ball ball2 = childState.getBall();
	
		engine.addEntity(ball2.mEntity);

		// Position pos = ball2.getComponent(Position.class);

		// engine.addSystem(new Gravity());
		// engine.addSystem(new ForceApply());
		// engine.addSystem(new Movement());
		// engine.addSystem(new FricsionSystem());
		//
		// System.out.println("ball pos 1 " + pos);

			float deltaTime = 1f;

		ball2.mEntity.getComponent(Force.class).add(anAction.getForce().x, anAction.getForce().y, anAction.getForce().z);
			int i = 1;
		while (ball2.mEntity.getComponent(Velocity.class).len() > 0.1 || i == 1) {
				engine.update(deltaTime);
			// System.out.println(i);
				i++;
			}

		}

	}


