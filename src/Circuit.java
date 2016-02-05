import java.util.ArrayList;
import java.util.Collections;

public class Circuit {
	private int skillH;
	private int skillE;
	private int skillP;
	private int id;
	
	//Jugglers matched to this circuit
	public ArrayList<Juggler> matchedJugglers;
	
	public Circuit(int id, int skillH, int skillE, int skillP) {
		this.id = id;
		this.skillH = skillH;
		this.skillE = skillE;
		this.skillP = skillP;
		matchedJugglers = new ArrayList<Juggler>();
	}
	
	//Add jugglers to the list of matches, making sure they are sorted
	public void addJuggler(Juggler juggler){
		matchedJugglers.add(juggler);
		juggler.setAssigned(true);
		Juggler.jugglersConsideredForPref--;
		
		//make sure the circuit is always sorted best to worst
		Collections.sort(matchedJugglers, new ReversedComparator()); 
		
	}
	
	
	//Assume that the matchedJugglers are sorted
	public Juggler getWorst(){
		return matchedJugglers.get(matchedJugglers.size()-1);
	}
	
	//Remove the worst juggler in the match list when we need to add a better one in
	public Juggler removeWorst(){
		Juggler worst = getWorst();
		worst.setAssigned(false);
		Juggler.jugglersConsideredForPref++;
		matchedJugglers.remove(matchedJugglers.size()-1);
		return worst;
	}
	
	public String toString(){
		String output = "C" + id + " ";
		
		for (int i = 0; i < matchedJugglers.size();i++)
		{
			output += matchedJugglers.get(i).toString();
			if (i != matchedJugglers.size() - 1)
				output += ", ";
		}
		
		return output;
	}

	
	//Getters and setters down here
	public int getSkillH() {
		return skillH;
	}

	public void setSkillH(int skillH) {
		this.skillH = skillH;
	}

	public int getSkillE() {
		return skillE;
	}

	public void setSkillE(int skillE) {
		this.skillE = skillE;
	}

	public int getSkillP() {
		return skillP;
	}

	public void setSkillP(int skillP) {
		this.skillP = skillP;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Juggler> getMatchedJugglers() {
		return matchedJugglers;
	}

	public void setMatchedJugglers(ArrayList<Juggler> matchedJugglers) {
		this.matchedJugglers = matchedJugglers;
	}
	
	
}
