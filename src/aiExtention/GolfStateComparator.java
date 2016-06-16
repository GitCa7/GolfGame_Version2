package aiExtention;

import physics.components.Position;
import searchTree.StateComparator;

public class GolfStateComparator extends StateComparator<GolfState> {
	private boolean[][] exploredStatesData;
	private int unitSize;

	public GolfStateComparator(int xDim, int yDim, int unitSize) {
		this.exploredStatesData = new boolean[xDim / unitSize][yDim / unitSize];
		this.unitSize = unitSize;


	}

	@Override
	public boolean isStateExplored(GolfState searchState) {
		// System.out.println(exploredStatesData.length);
		// System.out.println(exploredStatesData[0].length);
		// System.out.println("ball x pos" + searchState.getBall().getComponent(Position.class).x / unitSize);

		int xStatePos = (int) (exploredStatesData[0].length / 2 - (searchState.getBall().mEntity.getComponent(Position.class).x / unitSize));
		int yStatePos = (int) ((exploredStatesData.length / 2 - searchState.getBall().mEntity.getComponent(Position.class).y
				/ unitSize));
		// System.out.println(xStatePos);
		return exploredStatesData[xStatePos][yStatePos];
	}

	@Override
	public void markExplored(GolfState searchState) {
		int xStatePos = (int) (exploredStatesData[0].length / 2 - (searchState.getBall().mEntity.getComponent(Position.class).x / unitSize));
		if(xStatePos < 0) {
			System.out.println("states length " + exploredStatesData.length);
			Position position = searchState.getBall().mEntity.getComponent(Position.class);
			double d = position.x / unitSize;
			System.out.println(position + " " + d);
		}
		int yStatePos = (int) ((exploredStatesData.length / 2 - searchState.getBall().mEntity.getComponent(Position.class).y
				/ unitSize));
//		if(searchState.getBall().getComponent(Position.class).x > 10000) {
//			System.out.println("xP " +xStatePos);
//		}
		exploredStatesData[xStatePos][yStatePos] = true;
	}

}
