package course.b1.restclient.json;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import course.b1.restclient.RentalsRestClientAbstract;

@SpringBootApplication
public class RestClientJson extends RentalsRestClientAbstract {

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return (args) -> {
			// obtain a token as the admin user
			String accessToken = getAccessToken("admin@rentals.io");
			post_searchUsers(accessToken);
			int userId = post_addUser(accessToken);
			System.out.println("userId: " + userId);

			put_updateUser(userId, accessToken);
			get_getUser(userId, accessToken);
			delete_deleteUser(userId, accessToken);
		};
	}

	// Makes a POST request with a body
	private void post_searchUsers(String accessToken) throws Exception {

		// get an access token to use with the request to test
		HttpHeaders headers = createHeadersForAccessToken(accessToken);

		String filter = "{\"email\":\"@rentals.io\"}";

		HttpEntity<String> request = new HttpEntity<String>(filter, headers);
		String result = this.restTemplate.postForObject(createURL("/api/rentals/users/search"), request, String.class);

		System.out.println("result: " + result);
	}

	// Makes a POST request to add a new user, the HTTP response
	// will have no body but a Location header with the URL for
	// the newly created user
	private int post_addUser(String accessToken) throws Exception {
		// get an access token to use with the request to test
		HttpHeaders headers = createHeadersForAccessToken(accessToken);

		// beside the ID we need to provide the role name in order for
		// the update to be successful - this is because the Role object
		// on the server has equality defined based on name not Id
		String newUser = "{"
				+ "\"email\":\"test1@rentals.com\", "
				+ "\"password\":\"pass\", "
				+ "\"roles\": "
				 + "["
					+ "{"
						+ "\"id\": 2, "
						+ "\"name\":\"CLIENT\""
					 + "}"
				 + "]"
				+ "}";
		HttpEntity<String> request = new HttpEntity<String>(newUser, headers);
		ResponseEntity<Void> result = this.restTemplate.exchange(createURL("/api/rentals/users"), HttpMethod.POST,
				request, Void.class);

		List<String> location = result.getHeaders().get("Location");

		System.out.println("result: " + location);

		String loc = location.get(0);

		return Integer.parseInt(loc.substring(loc.lastIndexOf('/') + 1, loc.length()));
	}

	// Makes a PUT request to update a user's roles and password
	private void put_updateUser(int userId, String accessToken) throws Exception {
		// get an access token to use with the request to test
		HttpHeaders headers = createHeadersForAccessToken(accessToken);

		String updatedUser = "{"
				+ "\"id\":\"" + userId + "\"," 
				+ "\"email\":\"test1@rentals.com\", "
				+ "\"password\":\"pass1\", "
				+ "\"roles\": "
				 + "["
					+ "{"
						+ "\"id\": 2, "
						+ "\"name\":\"CLIENT\""
					+ "},"
					+ "{"
						+ "\"id\": 1, "
						+ "\"name\":\"CLERK\""
				    + "}"	
				 + "]"
				+ "}";
		HttpEntity<String> request = new HttpEntity<String>(updatedUser, headers);
		ResponseEntity<String> result = this.restTemplate.exchange(createURL("/api/rentals/users/" + userId),
				HttpMethod.PUT, request, String.class);

		System.out.println("result: " + result);
	}

	// Makes a DELETE request to delete a user
	private void delete_deleteUser(int userId, String accessToken) throws Exception {
		// get an access token to use with the request to test
		HttpHeaders headers = createHeadersForAccessToken(accessToken);

		HttpEntity<Void> request = new HttpEntity<Void>(headers);
		ResponseEntity<Void> result = this.restTemplate.exchange(createURL("/api/rentals/users/" + userId),
				HttpMethod.DELETE, request, Void.class);

		System.out.println("result: " + result.getStatusCodeValue());
	}

	// Makes a GET request to retrieve a user's details
	private void get_getUser(int userId, String accessToken) throws Exception {
		// get an access token to use with the request to test
		HttpHeaders headers = createHeadersForAccessToken(accessToken);

		HttpEntity<Void> request = new HttpEntity<Void>(headers);
		ResponseEntity<String> result = this.restTemplate.exchange(createURL("/api/rentals/users/" + userId),
				HttpMethod.GET, request, String.class);

		System.out.println("result: " + result);
	}

	public static void main(String args[]) {
		SpringApplication.run(RestClientJson.class);
	}
}
