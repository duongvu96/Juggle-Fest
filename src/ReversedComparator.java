import java.util.Comparator;

/*
 * A class we want to use to implement sorting jugglers from best to worse
 */
public class ReversedComparator implements Comparator<Juggler>{

	public int compare(Juggler j1, Juggler j2) {
		return j2.compareTo(j1);
	}
}