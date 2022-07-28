package fileParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import programGenerator.Dance;
import programGenerator.Dancer;
import programGenerator.Group;
import programGenerator.Member;

public class DanceFileReader {
	
	private HashMap<String, Group> groups;
	
	public DanceFileReader() {
		
	}

	/**
	 * Setup the dance groups from a file
	 * @param danceGroupFilePath filepath of the file with the dance groups
	 * @return hashmap of key: Group name Value: The group of dancers in the group
	 */
	public HashMap<String, Group> setupGroups(String danceGroupFilePath) 
	{
		FileParser fp = new FileParser(danceGroupFilePath);
		//Go through each line and add each line to the group hash map
		groups = new HashMap<String, Group>();
		//first line with "Dance Group" then "Members", discard first input
		fp.getInput();
		String[] inp = fp.getInput();
		
		
		
		while (inp != null) {
			//[0] group name, [1] dancers split by commas
			
			groups.put(inp[0].trim(), new Group(inp [0].trim(), inp[1].trim()));
			inp = fp.getInput();
		}
		
		return groups;
		
		
	}

	/**
	 * Creates a new HashMap of Dances
	 * @param dancesFilePath filepath to the file which has the dances
	 * @return hashmap, key: Dance name, Value: The dancers and dance groups in the dance
	 */
	public HashMap<String, Dance> setupDances(String dancesFilePath) {
		if (groups == null) {
			throw new IllegalStateException("Must setup groups before setting up dances");
		}
		FileParser fp = new FileParser(dancesFilePath);
		HashMap<String, Dance> dances = new LinkedHashMap<String, Dance>();// for nathan changed to linkedhashmap so it works with 3 it effects ur method
		//go through each line and add each line to dances list
		fp.getInput();
		String[] inp = fp.getInput();
		
		
		//for each line, [0] is name, [1] is dance groups and solo dancers delimited by ","
		//Need to pass a collection of dance groups and dancers (members) to create a dance
		while (inp != null) {
			String[] memberNames = inp[1].split(",");
			List<Member> members = new ArrayList<Member>();
			for (String name : memberNames) {

				if (groups.keySet().contains(name.trim())) {
					members.add(groups.get(name.trim()));

				} else {
					members.add(new Dancer(name.trim()));
				}
			}
			
			dances.put(inp[0].trim(), new Dance(inp[0].trim(), members));
			inp = fp.getInput();
		}
		
		return dances;
		
	}
	

	
}
