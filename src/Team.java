import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Team implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private double elo;
	private String name;
	private int gamesPlayed, division;
	private List<Double> oldElos;
	
	public Team(String name, double elo, int div) {
		this.name = name;
		this.elo = elo;
		division = div;
		gamesPlayed = 0;
	}
	
	public Team(String name, int div) {
		this.name = name;
		this.elo = 1000;
		division = div;
		gamesPlayed = 0;
	}
	
	public Team(String name) {
		this.name = name;
		this.elo = 1000;
		division = -1;
		gamesPlayed = 0;
	}
	
	@Override
	public String toString() {
		return name +": elo-"+ elo + " gp-"+gamesPlayed + " div: "+division;
	}
	
	public String getRecordString() {
		if(oldElos == null) {
			oldElos = new ArrayList<>();
		}
		
		String out = "Past Elos: ", delim = "";
		for(double e: oldElos) {
			out += delim + e;
			delim = ", ";
		}
		
		return out;
	}
	
	public Team copy() {
		return new Team(this.name, this.elo, this.division);
	}
	
	public int getDiv() {
		return division;
	}
	
	public void setDiv(int div) {
		division = div;
	}
	
	public double getElo() {
		return elo;
	}
	
	public String getName() {
		return name;
	}
	
	public void setElo(double e) {
		elo = e;
		
		if(oldElos == null) {
			oldElos = new ArrayList<>();
		}
		
		oldElos.add(elo);
	}
	
	public int getGamesPlayed() {
		return gamesPlayed;
	}
	
	public void playGame() {
		gamesPlayed++;
	}
	
	public void resetGamesPlayed() {
		gamesPlayed = 0;
	}
	
	public void recordWeek(int i) {
		if(i != gamesPlayed) {
			gamesPlayed++;
			if(i != gamesPlayed) {
				gamesPlayed--;
				System.err.println("Error: Team "+name+" attempted to record week"+i+" but has played "+gamesPlayed+" games.");
				return;
			}
		}
		
		if(oldElos == null) {
			oldElos = new ArrayList<>();
		}
		
		oldElos.add(elo);
	}
}
