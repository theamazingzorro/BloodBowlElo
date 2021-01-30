import java.util.Comparator;

public class TeamGamesComparator implements Comparator<Team>{
	 @Override
	    public int compare(Team left, Team right) {
	        return left.getGamesPlayed() - right.getGamesPlayed();
	    }
}
