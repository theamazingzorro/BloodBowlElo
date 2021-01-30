import java.util.Comparator;

public class TeamNameComparator implements Comparator<Team>{
	@Override
    public int compare(Team left, Team right) {
        return left.getName().compareTo(right.getName());
    }
}
