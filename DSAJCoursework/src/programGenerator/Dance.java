package programGenerator;

import java.util.List;

public class Dance 
{
	private List<Member> members;
	private String name;
	
	

	/**
	 * Creates a dance with a given list of performers and gives the dance a name
	 * 
	 * @param name the name of the dance
	 * @param members A list of Soloists and Dancers in this dance
	 */
	public Dance(String name, List<Member> members) 
	{
		this.members = members;
		this.name = name;
	}
	
	/**
	 * Returns a String which contains the names of the dancers in this dance, delimited by ","
	 * @return names of all dancers delimited by ","
	 */
	public String getNames() 
	{
		String list = ""; 
		
		for(Member member : members) 
		{
			list += member.getNames() + ",";
		}
		return list;
	}
	
	/**
	 * Returns all the dancers in this group
	 * @return A List of Members which contains all groups and soloists in the dance
	 */
	public List<Member> getMembers(){
		return members;
	}

	/**
	 * Gets the dance's name
	 * @return String of the dance's name
	 */
	public String getDanceName() {
		return name;
	}
}
