package course.b1.restclient.java;

public class Role {

	public enum Name {
		CLERK, CLIENT, ADMIN
	}
	
	private Integer id;
	
	private Name name;
	
	
	public Role() {
		super();
	}

	public Role(Name name) {
		super();
		this.name = name;
	}

	public Role(Integer id, Name name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
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
		Role other = (Role) obj;
		if (name != other.name)
			return false;
		return true;
	}
}
