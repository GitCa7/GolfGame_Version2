package framework;

import aiExtention.GolfAction;
import aiExtention.GolfState;
import aiExtention.Utils.GolfSearchData;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import framework.constants.CompoMappers;
import framework.entities.Player;
import physics.components.Position;
import physics.entities.Ball;
import searchTree.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Player observer class allowing the bot to react to the game by applying
 * a force on his ball, once it is the bot's turn.
 * created 16.06.16
 *
 * @author martin
 */
public class BotObserver extends PlayerObserver
{

    public BotObserver()
    {
        forceListToSolution = new LinkedList<>();
    }

    @Override
    public Vector3 getForce(Game state)
    {
        Ball myBall = state.getBall(getPlayer());
        Vector3 goalPos = CompoMappers.GOAL.get(myBall.mEntity).mGoalSpace.getPosition();
        Entity target = new Entity();
        target.add(new Position(goalPos.x, goalPos.y, goalPos.z));

        GolfSearchData searchPerformer= new GolfSearchData(myBall, target);

        TreeNode<GolfState, GolfAction> solutionNode = searchPerformer.greedySolution();

        extractSolution(solutionNode);

        return forceListToSolution.pollFirst();
    }

    /**
     * @param leaf the node considered leaf,
     * adds the series of forces need  to be applied to the root state to reach the leaf state to the forceListToSolution List
     */
    public void  extractSolution(TreeNode<GolfState, GolfAction> leaf)
    {
        int depthCounter=0;
        TreeNode<GolfState, GolfAction> tempNode = leaf;
        while(depthCounter<leaf.getNodeDeapth()){
            while(tempNode.getParent().getNodeDeapth()!=depthCounter){
                tempNode=tempNode.getParent();
                depthCounter++;
            }
            forceListToSolution.add(tempNode.getAction().getForce());
        }
    }

    private LinkedList<Vector3> forceListToSolution;
}
