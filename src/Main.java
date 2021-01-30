import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
 * ToDo
 * csv output? so i can graph this shit overtime in excel or smth
 */

public class Main {

	private static League l;
	private static EloFile file;
	private static EloPredicter ep = new EloPredicter(20);
	private static boolean cont = true;
	
	/*
	 * Current Leagues:
	 *  	-ftc
	 *  	-ftcplayofftest
	 */
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		do {
			System.out.println();
			System.out.print("> ");
			
			//breaks up command into array of space separated words
			String[] input = scan.nextLine().replaceAll("\\p{javaSpaceChar}{2,}"," ").toLowerCase().split(" ");
			
			switch(input[0]) {
				case "exit": exit(); break;
				case "show": displayTeam(input[1]); break;
				case "add": addTeam(input[1]); break;
				case "addelo": addTeamWithElo(input[1], input[2], input[3]); break;
				case "newleague": createNewLeague(input[1]); break;
				case "record": recordOutcome(input[1], input[2], input[3], input[4]); break;
				case "predict": displayOutcomes(input[1], input[2]); break;
				case "showall": displayTeams(); break;
				case "load": read(input[1]); break;
				case "save": save(); break;
				case "saveas": saveAs(input[1]); break;
				case "count": displayTeamCount(); break;
				case "ranking": displayTeamsByElo(); break;
				case "divranking": displayTeamsByEloInDiv(input[1]); break;
				case "gamesplayed": displayTeamsByGamesPlayed(); break;
				case "byeweek": byeWeek(input[1]); break;
				case "delete": removeTeam(input[1]); break;
				case "freedivs": freeDivs(); break;
				case "setdiv": setTeamDiv(input[1], input[2]); break;
				case "averages": displayDivAvgElos(); break;
				case "endcomp": resetSchedule(); break;
				case "closediv": endDiv(input[1]); break;
				case "playoffpreds": predictPlayoffs(scan); break;
				case "regresstomean": rolloverElo(); break;
				default:
					System.out.println("Sorry, command not recognized.");
			}

			
		} while(cont);
		
		scan.close();
	}
	
	private static void endDiv(String d) {
		int i = Integer.parseInt(d);
		l.resetDivSchedule(i);
		l.freeDiv(i);
		
		System.out.println("Div "+i+" closed.");
	}
	
	private static void rolloverElo() {
		l.rolloverElo(0.8);
		System.out.println("Elo reduced towards 1000 by 20%.");
	}
	
	private static void predictPlayoffs(Scanner scan) {
		System.out.println("How many teams are participating?");
		System.out.print("> ");
		int teamCount = scan.nextInt();
		scan.nextLine();
		
		System.out.println("Enter teams in order of playoff bracket appearance. [or use 'exit' to exit]");
		ArrayList<String> teams = new ArrayList<>();
		while(teams.size() < teamCount) {
			System.out.print("\n> ");
			String name = scan.nextLine().trim().toLowerCase();
			if(name.equals("exit")) {
				System.out.println("Shutting out of playoff prediction.");
				return;
			}
			
			if(!l.hasTeam(name)) {
				System.out.println("Error: team "+name+" not recognized. Please try again.");
			} else if(teams.contains(name)) {
				System.out.println("Error: team "+name+" already added to playoffs. Please try again.");
			} else {
				teams.add(name);
			}
		}
		
		System.out.println("Running Predictions...");
		
		PlayoffBracketPredicter pbPred = new PlayoffBracketPredicter(l, teams);
		
		HashMap<String, Double> results = pbPred.runSimulation(1000000);
		ArrayList<Map.Entry<String, Double>> resultsSorted = new ArrayList<>(results.entrySet());
		resultsSorted.sort(Map.Entry.comparingByValue());
		
		System.out.println("\nOdds of winning (desc.):");
		for(int i =resultsSorted.size()-1; i >= 0; i--) {
			System.out.println(resultsSorted.get(i).getKey()+": "+resultsSorted.get(i).getValue());
		}
	}
	
	private static void resetSchedule() {
		l.resetSchedule();
	}
	
	private static void setTeamDiv(String name, String s) {
		if(!l.hasTeam(name)) {
			System.out.println("Error: team "+name+" not recognized.");
			return;
		}
		int i = Integer.parseInt(s);
		
		l.getTeam(name).setDiv(i);
		System.out.println("Team "+name+" has been moved to div "+i);
	}
	
	private static void freeDivs() {
		l.freeDivs();
		System.out.println("All divs emptied.");
	}
	
	private static void removeTeam(String name) {
		if(!l.hasTeam(name)) {
			System.out.println("Error: team "+name+" not recognized.");
			return;
		}
		
		l.removeTeam(name);
	}
	
	private static void byeWeek(String name) {
		if(!l.hasTeam(name)) {
			System.out.println("Error: team "+name+" not recognized.");
			return;
		}
		l.getTeam(name).playGame();
		System.out.println("Team "+name+" bye week recorded.");
	}
	
	private static void displayTeamsByGamesPlayed() {
		System.out.println();
		System.out.println(l.sortedByGamesPlayed());
	}
	
	private static void displayDivAvgElos() {
		System.out.println();
		System.out.println("Premier Upper Bracket average Elo: " + l.divAvgElo(1));
		System.out.println("Premier Lower Bracket average Elo: " + l.divAvgElo(2));
		System.out.println("Younglings Upper Bracket average Elo: " + l.divAvgElo(3));
		System.out.println("Younglings Lower Bracket average Elo: " + l.divAvgElo(4));
	}
	
	private static void displayTeamsByEloInDiv(String s) {
		int i = Integer.parseInt(s);
		
		System.out.println();
		System.out.println(l.sortedByEloInDiv(i));
	}
	
	private static void displayTeamsByElo() {
		System.out.println();
		System.out.println(l.sortedByElo());
	}
	
	private static void displayTeamCount() {
		System.out.println("The league contains "+l.teamCount()+" teams.");
	}
	
	private static void exit() {
		System.out.println("Exiting. . . ");
		cont = false;
	}
	
	private static void displayTeam(String name) {
		if(!l.hasTeam(name)) {
			System.out.println("Error: team "+name+" not recognized.");
			return;
		}
		System.out.println(l.getTeam(name));
		System.out.println(l.getTeam(name).getRecordString());
	}
	
	private static void addTeam(String name) {
		if(l.hasTeam(name)) {
			System.out.println("Error: team "+name+" a.");
			return;
		}
		l.addTeam(new Team(name));
		System.out.println("Team "+name+" created.");
	}
	
	private static void addTeamWithElo(String name, String elo, String div) {
		int e = Integer.parseInt(elo);
		int d = Integer.parseInt(div);
		if(l.hasTeam(name)) {
			System.out.println("Error: team "+name+" a.");
			return;
		}
		l.addTeam(new Team(name, e, d));
		System.out.println("Team "+name+" created.");
	}
	
	private static void createNewLeague(String s) {
		file = new EloFile(s);
		l = new League();
		System.out.println("League "+s+" created.");
	}
	
	private static void recordOutcome(String a, String b, String outcome, String gp) {
		if(!l.hasTeam(a)) {
			System.out.println("Error: team "+a+" not recognized.");
			return;
		}
		if(!l.hasTeam(b)) {
			System.out.println("Error: team "+b+" not recognized.");
			return;
		}
		
		Team A = l.getTeam(a);
		Team B = l.getTeam(b);
		
		try {
			int games = Integer.parseInt(gp) - 1;
			if(A.getGamesPlayed() != games || B.getGamesPlayed() != games) {
				System.out.println("Error: Week "+gp+" already recorded.");
				return;
			}
		} catch (Error e) {
			System.out.println("Error: " + gp + " is not a number.");
		}
		
		
		switch(outcome) {
			case "win": ep.recordOutcome(A, B, 1);
				break;
			case "loss": ep.recordOutcome(A, B, 0);
				break;
			case "draw": ep.recordOutcome(A, B, 0.5);
				break;
			default:
				System.out.println("Error: "+outcome+" is not a recognized outcome.");
				return;
		}
		System.out.println("Game recorded.");
	}
	
	private static void displayOutcomes(String a, String b) {
		if(!l.hasTeam(a)) {
			System.out.println("Error: team "+a+" not recognized.");
			return;
		}
		if(!l.hasTeam(b)) {
			System.out.println("Error: team "+b+" not recognized.");
			return;
		}
		System.out.println();
		ep.outcomes(l.getTeam(a),l.getTeam(b));
	}
	
	private static void displayTeams() {
		System.out.println();
		System.out.println(l);
	}
	
	private static void read(String s) {
		file = new EloFile(s);
		l = file.read();
		System.out.println("Read Successful.");
	}
	
	private static void saveAs(String s) {
		file = new EloFile(s);
		System.out.println("New file name <"+s+"> accepted.");
		save();
	}
	
	private static void save() {
		file.write(l);
		System.out.println("Save Successful.");
	}
}
