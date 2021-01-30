import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class League implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Map<String, Team> teams = new HashMap<>();
	
	public void addTeam(Team a) {
		teams.put(a.getName(), a);
	}
	
	public Team getTeam(String name) {
		return teams.get(name);
	}
	
	public void removeTeam(String name) {
		teams.remove(name);
	}
	
	public void freeDivs() {
		for(Team t : teams.values()) {
			t.setDiv(-1);
		}
	}
	
	public void freeDiv(int div) {
		for(Team t : teams.values()) {
			if(t.getDiv() == div)
				t.setDiv(-1);
		}
	}
	
	public int teamCount() {
		return teams.size();
	}
	
	public double divAvgElo(int div) {
		int teamCount = 0;
		double totalElo = 0;
		for(Team t: teams.values()) {
			if(t.getDiv() == div) {
				teamCount++;
				totalElo+=t.getElo();
			}
		}
		
		return totalElo / teamCount;
	}
	
	public String sortedByGamesPlayed() {
		return toStringSortedInAnyDiv(new TeamGamesComparator());
	}
	
	public String sortedByElo() {
		return toStringSortedInAnyDiv(new TeamEloComparator());
	}
	
	public String sortedByEloInDiv(int div) {
		return toStringSortedInDiv(new TeamEloComparator(), div);
	}
	
	private String toStringSorted(Comparator<Team> c) {
		String out = "", sep="";
		ArrayList<Team> ts =new ArrayList<>(teams.values());
		
		if(c == null) {
			System.out.println("Problems");
		}
		Collections.sort(ts, c);
		for(Team t: ts) {
			out+=sep + t.toString();
			sep = "\n";
		}
		
		return out;
	}
	
	private String toStringSortedInAnyDiv(Comparator<Team> c) {
		String out = "", sep="";
		ArrayList<Team> ts =new ArrayList<>(teams.values());
		
		if(c == null) {
			System.out.println("Problems");
		}
		Collections.sort(ts, c);
		for(Team t: ts) {
			if(t.getDiv() != -1) {
				out+=sep + t.toString();
				sep = "\n";
			}
		}
		
		return out;
	}
	
	private String toStringSortedInDiv(Comparator<Team> c, int div) {
		String out = "", sep="";
		ArrayList<Team> ts =new ArrayList<>(teams.values());
		
		if(c == null) {
			System.out.println("Problems");
		}
		Collections.sort(ts, c);
		for(Team t: ts) {
			if(t.getDiv() == div) {
				out+=sep + t.toString();
				sep = "\n";
			}
		}
		
		return out;
	}
	
	@Override
	public String toString() {
		return toStringSorted(new TeamNameComparator());
	}
	
	public boolean hasTeam(String s) {
		return teams.containsKey(s);
	}
	
	public void endWeek(int i) {
		for(Team t: teams.values()) {
			t.recordWeek(i);
		}
	}
	
	public void rolloverElo(double percent) {
		for(Team t: teams.values()) {
			t.setElo(1000. + (t.getElo() - 1000.) * percent);
		}
	}
	
	public void resetDivSchedule(int div) {
		for(Team t: teams.values()) {
			if(t.getDiv() == div)
				t.resetGamesPlayed();
		}
	}
	
	public void resetSchedule() {
		for(Team t: teams.values()) {
			t.resetGamesPlayed();
		}
	}
}
