package programGenerator;

public class Dancer extends Member {

	public Dancer(String name) {
		super(name);
	}

	@Override
	public String getNames() 
	{
		return name;
	}

}
