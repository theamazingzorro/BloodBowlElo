
public class EloPredicter {
	private int k;
	
	public EloPredicter(int k) {
		this.k = k;
	}
	
	/*
	 * Odds that team A will beat team B
	 */
	public double expectedResult(Team a, Team b) {
		return 1.0 / (1.0 + Math.pow(10, ((b.getElo() - a.getElo()) / 150)));
	}
	
	public void outcomes(Team a, Team b) {
		System.out.println(a.getName()+ " will win "+100*expectedResult(a,b)+"% of the time changing elos to:");
		System.out.println(a.getName()+": "+newElo(a,b,1));
		System.out.println(b.getName()+": "+newElo(b,a,0));
		
		System.out.println(b.getName()+ " will win "+100*expectedResult(b,a)+"% of the time changing elos to:");
		System.out.println(a.getName()+": "+newElo(a,b,0));
		System.out.println(b.getName()+": "+newElo(b,a,1));
		
		System.out.println("A draw will change elos to:");
		System.out.println(a.getName()+": "+newElo(a,b,.5));
		System.out.println(b.getName()+": "+newElo(b,a,.5));
	}
	
	public void recordOutcome(Team a, Team b, double aVal) {
		double bVal = 1 - aVal;
		double aNew = newElo(a,b,aVal);
		double bNew = newElo(b,a,bVal);
		
		a.setElo(aNew);
		b.setElo(bNew);
		a.playGame();
		b.playGame();
	}
	
	/*
	 * aVal = 1 if a won, .5 if draw, 0 if a lost
	 */
	public double newElo(Team a, Team b, double aVal) {
		double oldElo = a.getElo();
		double expectedVal = expectedResult(a,b);
		return oldElo + k * (aVal - expectedVal);
	}
}
