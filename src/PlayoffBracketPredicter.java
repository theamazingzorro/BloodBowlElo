import java.util.ArrayList;
import java.util.HashMap;

public class PlayoffBracketPredicter {
	
	private HashMap<String, Integer> teamWinFreq;
	private ArrayList<String> teams;
	private League league;
	private EloPredicter pred = new EloPredicter(20);
	
	/*
	 * Note teams must be in bracket order
	 */
	public PlayoffBracketPredicter(League l, ArrayList<String> teams) {
		league = l;
		this.teams = teams;
		
		clearWinFreq();
	}
	
	private void clearWinFreq() {
		teamWinFreq = new HashMap<>();
		
		for(String t: teams) {
			teamWinFreq.put(t, 0);
		}
	}
	
	private League getNewLeagueCopy() {
		League l = new League();
		
		for(String name: teams) {
			Team t = league.getTeam(name).copy();
			l.addTeam(t);
		}
		
		return l;
	}
	
	public HashMap<String, Double> runSimulation(int reps) {
		clearWinFreq();
		
		for(int i = 0; i < reps; i++) {
			League tempLeague = getNewLeagueCopy();
			ArrayList<String> remainingTeams = new ArrayList<String>(teams);
			
			while(remainingTeams.size() > 1) {
				for(int j = 0; j+1 < remainingTeams.size(); j++) {
					Team a = tempLeague.getTeam(remainingTeams.get(j));
					Team b = tempLeague.getTeam(remainingTeams.get(j + 1));
					
					if(Math.random() <= pred.expectedResult(a, b)) {
						remainingTeams.remove(j + 1);
						pred.recordOutcome(a, b, 1);
					} else {
						remainingTeams.remove(j);
						pred.recordOutcome(a, b, 0);
					}
				}
			}
			
			String winner = remainingTeams.get(0);
			
			//System.out.println(tempLeague.sortedByElo()+"\n");
			
			teamWinFreq.replace(winner, teamWinFreq.get(winner) + 1);
		}
		
		HashMap<String, Double> winningOdds = new HashMap<>();
		for(String n: teamWinFreq.keySet()) {
			winningOdds.put(n, (double) teamWinFreq.get(n) / reps);
		}
		
		return winningOdds;
	}
}
