package programGenerator;

public abstract class Member 
{
	//Records the last time this member has performed
	protected int timeLastPerformed;
	protected String name;
	
	/**
	 * Returns a string with all of the names of this member delimited by ","
	 * 
	 * @return String with names concatenated together, delimiteb by a ","
	 */
	public abstract String getNames();
	
	public Member(String name) {
		this.name = name;
	}
	
	/**
	 * Returns time when this performer last performed
	 * @return an int representing the time this member last performed 
	 */
	public int getTimeLastPerformed() {
		return timeLastPerformed;
	}
	
	/**
	 * Sets the time the performer last performed 
	 * @param time int representing the time the performer last danced 
	 */
	public void setTimeLastPerformed(int time) {
		timeLastPerformed = time;
	}
	
	/**
	 * Returns the name of the group or soloist 
	 * 
	 * @return String name of the group or dancer performing
	 */
	public String getName() {
		return name;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
}
