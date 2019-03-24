package course.b1.restclient.java;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class User {
	
	private Integer id;
	
	private String email;
	
	private String password;
		
	private Set<Role> roles = new HashSet<Role>();
	
	public User() {
		super();
	}

	public User(String email) {
		super();
		this.email = email;
	}
	
	public User(String email, String password, Role... roles) {
		this(null, email, password, roles);
	}

	public User(Integer id, String email, String password, Role... roles) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.roles.addAll(Arrays.asList(roles));
	}
	
	public User(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void addRole(Role role) {
		
		this.roles.add(role);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
