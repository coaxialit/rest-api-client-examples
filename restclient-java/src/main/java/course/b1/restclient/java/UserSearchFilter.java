package course.b1.restclient.java;

import org.springframework.util.StringUtils;

public class UserSearchFilter {
	/**
	 * Title fragment.
	 */
	private String email;
	
	public UserSearchFilter() {
		super();
	}
	public UserSearchFilter(String email) {
		super();
		this.email = email;
	}


	public String getEmail() {
		return email;
	}


	public boolean isEmpty() {
		return StringUtils.isEmpty(this.email);
	}

}
