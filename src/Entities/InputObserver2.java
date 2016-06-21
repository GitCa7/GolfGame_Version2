package Entities;


public class InputObserver2{

	//The Observer needs a followCamera thats updated whenever MouseInput is receives
	private FollowCamera follow;
	
	//The Observer needs a free Camera in case he wants to change the perspective
	private Camera freeCam;
	
	// The camera´s Input is processed for a certain player
	private Player mPlayer;
	
	//The currentZoomlevel, as soon as that changes the value needs to be updated in the follow camera
	private float lastZoomLvl = 100f;
	
	//Value for scaling mouseWheel Value
	private static final float zoomMult = 0.3f;
	
	//Angle around the player
	private float angleAroundPlay = 0;
	
	private Vector3 direction = new Vector3(0,0,1);
	
	
	public InputObserver(FollowCamera follow, freeCamera freeCam)	{
		//Move method of te camera needs to be updated as soon as The MouseButton is pressed
		this.follow = follow;
		this.freeCam = freeCam;
	}
	
	
	@Override
	//Should be called whenever The left MOuse buton is pressed or The mouseWheel is turned
	public void update(Game state) {
		
		if(Mouse.isButtonDown(1))	{
			float pitchChange = Mouse.getDY() * 0.2f;
			follow.setPitch(pitchChange);
			
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlay -= angleChange;
			
			follow.changePitch();
			
			
		}
		
		if(Mouse.getDWheel() * zoomMult != lastZoomLvl)	{
			lastZoomLvl = Mouse.getDWheel() * zoomMult;
			follow.setZoom(lastZoomLvl);
		}
		
		updatePosition();
		updateForceDirection();
	}
	
	
	private void updatePosition()	{
		float horizontalDistance = follow.calculateHorizontalDistance();
		float verticalDistance = follow.calculateVerticalDistance();
		follow.calculateCameraPosition(horizontalDistance, verticalDistance);
		follow.changePostionAroundPlayer();
	}
	
	private void updateForceDirection()	{
		float yAngle = (-follow.getYaw()-180);
		direction.rotate(yAngle,0,1,0);
	}
	
	public void setPlayer(Player newPlayer)	{
		mPlayer = newPlayer;
	}
	
	public Player getPlayer()	{
		return mPlayer;
	}
	
	public Camera getFreeCam()	{
		return freeCam;
	}
	
	public FollowCamera getFollowCam()		{
		return follow;
	}
	
	public Vector3 getForceDirection()	{
		return direction;
	}

}
