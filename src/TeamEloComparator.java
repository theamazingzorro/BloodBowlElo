import java.util.Comparator;

public class TeamEloComparator implements Comparator<Team>{
	@Override
    public int compare(Team left, Team right) {
		if(right.getElo() > left.getElo())
			return 1;
		else if(right.getElo() < left.getElo())
			return -1;
		else
			return 0;
    }
}
