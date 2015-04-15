public class Captured_graveyard{
	public int fire;
	public int water;
	public Captured_graveyard(){
		fire = 0;
		water = 0;
	}
	public void bury_dead(int x){
		if (x == 0){
			fire += 1;
		}
		else {
			water += 1;
		}
	}
}