import java.util.*;
import java.io.*;

public class Assigner {
	
	private ArrayList<Circuit> circuits;
	private ArrayList<Juggler> jugglers;
	private String dataFile;
	
	public static int jugglersPerLine;
	private static long startTime = System.currentTimeMillis();
	
	public static final String DATA_FILE = "data.txt";
	public static final String OUTPUT_FILE = "output.txt";
	public static final String stringDummy = "initialLine";
	//The circuit for which we need to find the sum of jugglers' id's
	public static final int circuitToAnswer = 1970;
	
	public Assigner(String dataFile){
		circuits = new ArrayList<Circuit>();
		jugglers = new ArrayList<Juggler>();
		this.dataFile = dataFile;
		generate();
	}
	
	//generate circuits before jugglers, because we need them to calculate the match values
	private void generate(){
		try{
			File file = new File(dataFile);
			Scanner in = new Scanner(file);
			
			//set line to a dummy text that is not "" because "" will be the break between circuits and jugglers
			String line = stringDummy;
			
			//this loop reads in the circuits
			while (in.hasNextLine()){
				line = in.nextLine();
				
				if (line.equals(""))
					break;
				
				String[] tokens = line.split(" ");
				
				int id = Integer.parseInt(tokens[1].substring(1));
				int skillH = Integer.parseInt(tokens[2].substring(2));
				int skillE = Integer.parseInt(tokens[3].substring(2));
				int skillP = Integer.parseInt(tokens[4].substring(2));
				
				circuits.add(new Circuit(id, skillH, skillE, skillP));
			}
			
			//now line == ""
			
			// this reads in the jugglers
			while (in.hasNextLine()) {
				line = in.nextLine();
				
				String[] tokens = line.split(" ");
				
				int id = Integer.parseInt(tokens[1].substring(1));
				int skillH = Integer.parseInt(tokens[2].substring(2));
				int skillE = Integer.parseInt(tokens[3].substring(2));
				int skillP = Integer.parseInt(tokens[4].substring(2));
				
				ArrayList<Integer> preferences = new ArrayList<Integer>();
				String[] prefTokens = tokens[5].split(",");
				
				for (int i = 0; i < prefTokens.length; i++)
				{
					int prefNumber = Integer.parseInt(prefTokens[i].substring(1));
					preferences.add(prefNumber);
				}
				
				jugglers.add(new Juggler(id, skillH, skillE, skillP, preferences, circuits));
			}
			
			in.close();
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		
		//the initial number of jugglers competing for their preferences
		Juggler.jugglersConsideredForPref= jugglers.size(); 
		jugglersPerLine = jugglers.size() / circuits.size();
	}
	
	//During initialAssign, we want to let everyone have the chance to compete for their preferred circuits
	//Because not everyone gets their preferred circuits, some circuits will be left under-manned
	//The remaining jugglers will be sorted into those circuits in finalAssign
	private void initialAssign(){
		while(Juggler.jugglersConsideredForPref > 0) {
			for (int i = 0; i < jugglers.size(); i++){
				Juggler curJ = jugglers.get(i);
				ArrayList<Integer> preferences = curJ.getPreferences();
				
				//if this juggler is no longer eligible to attempt at the preferred circuits, skip
				//Update the number of jugglers still eligible 
				if (!curJ.consideredForPreferences())
				{
					Juggler.jugglersConsideredForPref--;
					continue;
				}
				
				// if current juggler is unassigned and still has preferences not considered
				if (!curJ.isAssigned()){
					for (int currentPref = 0; currentPref < preferences.size(); currentPref++){
						Juggler.currentCircuitForCompare = preferences.get(currentPref);
						Circuit currentCircuit = circuits.get(Juggler.currentCircuitForCompare);
					
						//if the circuit is under-manned, let this juggler in
						if (currentCircuit.getMatchedJugglers().size() < jugglersPerLine)
						{
							currentCircuit.addJuggler(curJ);
							break;
						}
						// if current juggler is better than someone in the circuit, 
						// the worst juggler in line must get out to make room
						else if (curJ.compareTo(currentCircuit.getWorst()) > 0)
						{
							currentCircuit.addJuggler(curJ);
							currentCircuit.removeWorst();
							break;
						}
						// do nothing if the circuit is full and there're no worse jugglers in line
					}
				}
			}
		}
	}
	
	//Collect the remaining jugglers and visit each under-manned circuit, sort the jugglers accordingly and fill it
	private void finalAssign(){
		ArrayList<Juggler> remainingJugglers = new ArrayList<Juggler>();
		for (Juggler juggler : jugglers)
		{
			if (!juggler.isAssigned())
				remainingJugglers.add(juggler);
		}
		
		for (int i = 0; i < circuits.size(); i++)
		{
			Juggler.currentCircuitForCompare = i;
			Circuit currentCircuit = circuits.get(i);
			if (currentCircuit.getMatchedJugglers().size() < jugglersPerLine){
				int remainingSpots = jugglersPerLine - currentCircuit.getMatchedJugglers().size();
				
				PriorityQueue<Juggler> jQueue = new PriorityQueue<Juggler>(remainingJugglers.size(), new ReversedComparator());
				jQueue.addAll(remainingJugglers);
				for (int j = 0; j < remainingSpots; j++){
					Juggler polled = jQueue.poll();
					currentCircuit.addJuggler(polled);
					remainingJugglers.remove(polled);
				}
			}
		}
	}
	
	//Use this to do all the assignments
	public void assign(){
		initialAssign();
		finalAssign();
	}
	
	public static void main(String[] args) {
		Assigner assigner = new Assigner(DATA_FILE);
		assigner.assign();

		//Write output text file;
		PrintWriter writer;
		try{
			writer = new PrintWriter("output.txt", "UTF-8");
			for (Circuit c : assigner.circuits)
				writer.println(c);
			writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Circuit circuit1970 = assigner.circuits.get(circuitToAnswer);
		int total = 0;
		for (Juggler j : circuit1970.getMatchedJugglers())
			total += j.getId();
		
		System.out.println("In circuit " + circuitToAnswer + ", the sum of all the jugglers' ids is " + total);
		long endTime = System.currentTimeMillis();
        System.out.println("It took " + (endTime - startTime) + " milliseconds");
	}
}
