package programGenerator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import astaire.Controller;
import astaire.TUI;
import fileParser.DanceFileReader;

public class ProgramGenerator implements Controller
{
	private HashMap<String, Group> groups;
	private HashMap<String, Dance> dances;
	
	private String danceGroupFilePath = "astaireDataFiles/danceShowData_danceGroups.csv";
	private String dancesFilePath = "astaireDataFiles/danceShowData_dances.csv";
	private DanceFileReader danceReader;
	private Random generator;

	public static void main(String[] arg) 
	{
		ProgramGenerator pg = new ProgramGenerator();
		TUI tUI = new TUI(pg);
	
	}

	/**
	 * Creates a new ProgramGenerator with the default dances and groups
	 */
	public ProgramGenerator() 
	{
		danceReader = new DanceFileReader();
		groups = setupGroups();
		dances = setupDances(dancesFilePath);
		generator = new Random();
		
	}


	/**
	 * Sets up hashMap of dances from a filepath of dances
	 * @param dFilePath filepath with the file containing the dances
	 * @return HashMap containing all the dances
	 */
	private HashMap<String, Dance> setupDances(String dFilePath) {	
		return danceReader.setupDances(dFilePath);
	}


	/**
	 * Sets up the groups 
	 * @return HashMap of all the groups that will perform in the dance sequence 
	 */
	private HashMap<String, Group> setupGroups() {
		return danceReader.setupGroups(danceGroupFilePath);
	}
	
	/**
	 * Lists the names of all performers in a specified dance.
	 * @param dance	a specified dance in the dance show
	 * @return the name of all performers that are in the specified dance. 
	 */
	@Override
	public String listAllDancersIn(String dance) {
		return dances.get(dance).getNames();
	}

	/**
	 * Lists all the dances and performers, with the dancers in alphabetical order 
	 * @return String containing all dances and all performers in alphabetical order 
	 */
	@Override
	public String listAllDancesAndPerformers() {

		String result = "";
		
		for (String name : dances.keySet()) {
			
			Dance dance = dances.get(name);
			String list = "";
			String [] dancersAndPerformers = dance.getNames().split(",");
			ArrayList<String> sortedDancers = new ArrayList<String>();
			
			list += "Dance:  " + dance.getDanceName() + "  Members: ";
			
			sortedDancers.add(dancersAndPerformers[0]);
			
			for(int i = 1; i < dancersAndPerformers.length; i++) 
			{
				int j = 0;
				while (j < sortedDancers.size()) {
					if (sortedDancers.get(j).compareTo(dancersAndPerformers[i]) > 0 ) {
						sortedDancers.add(j, dancersAndPerformers[i]);
						break;
					}
					j++;
				}
				
			}
			
			for(int i = 0; i <sortedDancers.size(); i++) 
			{
				list += sortedDancers.get(i) + ",";
			}
			
			result += list + "\n";
			
		}
		
		return result;
	}

	/**
	 * Checks if a given running order is feasable
	 * 
	 * @param filename filepath to the file containing the dance sequence
	 * @param gaps the number of gaps the dancers need between dances to get ready for the next one 
	 * @return whether a running order is possible or not 
	 */
	@Override
	public String checkFeasibilityOfRunningOrder(String filename, int gaps) 
	{
		boolean notFeasable=false; 
		 			

		int currentTime = 1;
		
		List<Dance> danceSequence = new ArrayList<Dance>();
		danceSequence.addAll(setupDances(filename).values());
		
		String out="";
		
		
		HashMap<String,Member>tiredDancers= new HashMap<String,Member>();
		
		
		for (Dance dance : danceSequence) {
			
			out+="\n "+ currentTime+" Dance  "+ dance.getDanceName();
			
			List<Member> dancers = dance.getMembers();
			for (Member member : dancers) {

				
				out+= " \n \t "+member.getName();
				
				if (tiredDancers.containsKey(member.name)) {
					out +=" can't perform, last dance at "+ (tiredDancers.get(member.getName()).getTimeLastPerformed()) ;
					//dance can't be done, try again later
					if (notFeasable==false) {
						
						notFeasable=true;
					}
				}
				
				tiredDancers.put(member.getName(),member);
				tiredDancers.get(member.getName()).setTimeLastPerformed(currentTime);
			}
			
			if (!tiredDancers.isEmpty()) {
				for(Iterator<HashMap.Entry<String, Member>> iterator = tiredDancers.entrySet().iterator(); 
						iterator.hasNext(); ) {
				    HashMap.Entry<String, Member> entry = iterator.next();
				    if(entry.getValue().getTimeLastPerformed()  + gaps <= currentTime) {
				    	
				    	iterator.remove();
				    }
				}
		}
			currentTime++;
		}

		if (notFeasable==true) {
			
			out="not feasable\n"+out;
		} else {
			out = "Feasable \n" +out;
		}
		
			return out;
	}
	
	/**
	 * Generates a running order
	 * 
	 * @param gaps the number of dances in between each dance a dancers needs to prepare for the next one 
	 * @return string with potential running order 
	 */
	@Override
	public String generateRunningOrder(int gaps) {
		int currentTime = 1;
		//List of the actual dance sequence
		List<Dance> danceSequence = new LinkedList<Dance>();
		//Keeps track of the dancers that have performed and are still changing 
		Queue<Member> tiredDancers = new LinkedList<Member>();
		//List to hold all dances
		List<Dance> dances = new LinkedList<Dance>();
		dances.addAll(this.dances.values());
		//Dances that were not able to be added due to dancers still changing 
		List<Dance> bufferedDances = new LinkedList<Dance>();
		while (!dances.isEmpty() || !bufferedDances.isEmpty()) {//Whilst dances still remain
			boolean addedBufferedDancer = false;
			int size = bufferedDances.size();
			//Attempt to add a sance
			for (int i = 0; i < size; i++) {
				Dance dance = bufferedDances.remove(0);
				List<Member> dancers = dance.getMembers();
				boolean danceCanBeAdded = true;
				//Checks to see if the dancers in the dance are ready to go
				for (Member member : dancers) {
					if (tiredDancers.contains(member)) {
						//dance can't be done, try again later
						danceCanBeAdded = false;
					}
				}
				//If dance can be added
				if (danceCanBeAdded) {
					//Add the dance
					danceSequence.add(dance);
					//Mark the dancers as tired and record the time they last performed, putting them to the end of the queue
					for (Member member : dance.getMembers()) {
						member.setTimeLastPerformed(currentTime);
						tiredDancers.add(member);
					}
					//Mark the dancers that are now ready to go as such
					while(tiredDancers.peek().getTimeLastPerformed() + gaps <= currentTime) {
						tiredDancers.remove().setTimeLastPerformed(0);;
					}
					//Increment timer 
					currentTime++;
					addedBufferedDancer = true;
				} else {
					//Try to add the dance later 
					bufferedDances.add(dance);
				}
			}
			if (!addedBufferedDancer && !dances.isEmpty()) {//Didn't managed to add any of the dances and still have dances remaining from the dance list
				//Add a new dance to the dances to try to add
				bufferedDances.add(dances.remove(0));
			} else if (!addedBufferedDancer && dances.isEmpty() && !bufferedDances.isEmpty()) {//still have remaining dances
				//Attempt to add dances again
				dances.addAll(bufferedDances);
				for (Dance d : danceSequence) {
					dances.add(generator.nextInt(dances.size()+1), d);
				}
				tiredDancers = new LinkedList<Member>();
				//resets tiredDancer queue
				currentTime = 1;
				bufferedDances = new LinkedList<Dance>();
				//resets Dance Sequence
				danceSequence = new LinkedList<Dance>();
			}
			
		}
	
		String out = "";
		for (Dance dance : danceSequence) {
			out += "Dance: " + dance.getDanceName() + "\n";
		}

		File file = new File("astaireDataFiles/danceShowDataNew_dances.txt");
		  
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(out);
			writer.close();
		   
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return out;
		
	}

}
