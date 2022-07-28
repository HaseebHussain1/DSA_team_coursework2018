package programGenerator;

public class Group extends Member
{
	//The list of dancers in this dance group
	private String[] dancerList; 
	
	/**
	 * Creates a group with a given name and the list of dancers
	 * 
	 * @param name Name of the dance group
	 * @param inp the list of names delimited by ", " that are in this dance group 
	 */
	public Group(String name, String inp) 
	{
		super(name);
		dancerList = inp.split(", ");
	}
	
	@Override
	public String getNames() 
	{
		String str = "";
		for (String s : dancerList) 
		{
			str += s + ",";
		}
		return str;
	}

}
