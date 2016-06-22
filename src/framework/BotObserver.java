package framework;

import aiExtention.GolfAction;
import aiExtention.GolfState;
import aiExtention.Utils.GolfSearchPerformer;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import framework.constants.CompoMappers;
import physics.components.Position;
import physics.constants.GlobalObjects;
import physics.constants.PhysicsCoefficients;
import physics.entities.Ball;
import physics.generic.Rounder;
import searchTree.TreeNode;

import java.util.LinkedList;

/**
 * Player observer class allowing the bot to react to the game by applying
 * a force on his ball, once it is the bot's turn.
 * created 16.06.16
 *
 * @author martin
 */
public class BotObserver extends PlayerObserver {

    public static final float BOT_DT = .02f;

    public BotObserver() {
        forceListToSolution = new LinkedList<>();
    }

    @Override
    public Vector3 getForce(Game state) {
        Rounder prevRounder = GlobalObjects.ROUND;

        //    GlobalObjects.ROUND = new Rounder(PhysicsCoefficients.AI_ARITHMETIC_PRECISION, PhysicsCoefficients.AI_ARITHMETIC_TOLERANCE);

        Ball myBall = state.getBall(getPlayer());
        Vector3 goalPos = CompoMappers.GOAL.get(myBall.mEntity).mGoalSpace.getPosition();
        Entity target = new Entity();
        target.add(new Position(goalPos.x, goalPos.y, goalPos.z));

        GolfSearchPerformer searchPerformer = new GolfSearchPerformer(state.getCurrentPlayers().get(0), state, BOT_DT);

        TreeNode<GolfState, GolfAction> solutionNode = searchPerformer.aStarSolution();
        System.out.println(solutionNode.getNodeDeapth() + "aaaaa");

        extractSolution(solutionNode);

        //    GlobalObjects.ROUND = prevRounder;

        return forceListToSolution.pollFirst();
    }

    /**
     * @param leaf the node considered leaf,
     *             adds the series of forces need  to be applied to the root state to reach the leaf state to the forceListToSolution List
     */
    public void extractSolution(TreeNode<GolfState, GolfAction> leaf) {

        TreeNode<GolfState, GolfAction> tempNode = leaf;
        forceListToSolution.add(leaf.getAction().getForce());
     for(int i=1;i<leaf.getNodeDeapth();i++)
        tempNode = tempNode.getParent();
        forceListToSolution.add(0,tempNode.getAction().getForce());

    }
        private LinkedList<Vector3> forceListToSolution;



}