import java.util.ArrayList;

public class Juggler implements Comparable {
	
	// set this to the circuit against which we want to compare our jugglers later
	public static int currentCircuitForCompare = -1;
	// i.e. jugglers not assigned and still haven't been considered for some of their preferred circuits
	// we will set this later after reading data
	public static int jugglersConsideredForPref = Integer.MAX_VALUE; 
	
	private ArrayList<Integer> preferences;
	private ArrayList<Integer> circuitMatch;
	
	// the number of preferred circuits this juggler has attempted for
	private int numPrefRemoved;
	private int skillH;
	private int skillE;
	private int skillP;
	private int id;

	// whether this juggler hasn't been considered for some preferred circuits
	private boolean hasPrefUnconsidered;
	
	//whether this juggler has been assigned to a circuit
	private boolean assigned;
	
	public Juggler(int id, int skillH, int skillE, int skillP, 
					ArrayList<Integer> preferences, ArrayList<Circuit> circuits){
		this.id = id;
		this.preferences = preferences;
		this.skillH = skillH;
		this.skillE = skillE;
		this.skillP = skillP;
		
		circuitMatch = new ArrayList<Integer>();
		
		for (Circuit circuit : circuits){
			circuitMatch.add(calculateMatch(circuit));
		}
		
		hasPrefUnconsidered = true;
		assigned = false;
		numPrefRemoved = 0;
	}

	// How to compare the jugglers
	public int compareTo(Object other) {
		if (other == null)
			return -100; //Error
		
		if (!other.getClass().equals(Juggler.class))
			return -100; //can't compare
		
		Juggler otherJuggler = (Juggler) other;

		return (this.circuitMatch.get(currentCircuitForCompare)
				.compareTo(otherJuggler.circuitMatch.get(currentCircuitForCompare)));

	}
	
	// Given a circuit, calculate the match score
	public int calculateMatch(Circuit circuit){
		int matchValue = this.skillE*circuit.getSkillE() + this.skillH*circuit.getSkillH() + 
						 this.skillP*circuit.getSkillP();
		return matchValue;
	}
	
	//Ticks off a preferred circuit this juggler has attempted to join
	public void removePreference(){
		numPrefRemoved++;
		
		if (numPrefRemoved == preferences.size())
			hasPrefUnconsidered = false;
	}
	
	public String toString(){
		
		String output = "J" + id /*" H:" + skillH + " E:" + skillE + " P:" + skillP*/;
		for (int preference : preferences)
			output += " C" + preference + ":" + circuitMatch.get(preference);
		return output;
	}
	
	//Check if we should let this juggler attempt to get in his preferred circuits
	public boolean consideredForPreferences(){
		return hasPrefUnconsidered && !assigned;
	}
	
	
	//Getters and Setters down here
	public ArrayList<Integer> getCircuitMatch() {
		return circuitMatch;
	}

	public void setCircuitMatch(ArrayList<Integer> circuitMatch) {
		this.circuitMatch = circuitMatch;
	}


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

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	public ArrayList<Integer> getPreferences(){
		return preferences;
	}
	
	public void setPreferences(ArrayList<Integer> preferences) {
		this.preferences = preferences;
	}
} 
