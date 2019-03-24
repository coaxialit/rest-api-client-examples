

import groovyx.net.http.RESTClient;

@Grapes([
	@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7')
])
class RestClientRentals {
	// Client ID and secret that identifies this client
	// for the REST API
	private final String clientId = 'rentals-client-id';
	private final String clientSecret = 'ddkl44#SDsfg';
	
	// Define a RESTClient instance with a root URL
	private def client = new RESTClient('http://localhost:8080/');
	
	
	/**
	 * Obtains an access token for the given email. This access token
	 * is used for subsequent requests to the RESTful API.
	 * @param email the email for which to obtain an access token
	 * @return an access token
	 */
	private String getAccessToken(String email) {
		// We are building here an HTTP POST request for URL /oauth/token
		// once we pass the required OAuth2 parameters we get back the access token
		// used for subsequent invocations of the REST API
		
		// Add basic authentication with clientid and clientsecret
		String credentials = clientId + ':' + clientSecret;
		client.headers['Authorization'] = 'Basic ' + credentials.bytes.encodeBase64();
		
		def response = client.post(
			path: '/oauth/token',
			requestContentType: 'application/x-www-form-urlencoded',
			body: [
					grant_type: 'password',
					username: email,
					password: 'pass',
					scope: 'read write'
				]
		);
	
		return response.data['access_token'];
	}

	/**
	 * Adds the Authorization header to the request.
	 * @param accessToken
	 */
	private void addHeadersForAccessToken(String accessToken) {
		client.headers['Authorization'] = 'Bearer ' + accessToken;
	}
	
	// Makes a POST request to search for all users with an email 
	// address @rentals.io
	private void post_searchUsers(String accessToken) {
		addHeadersForAccessToken(accessToken);
		
		// retrieve all users with an email @rentals.io
		def result = client.post(
			path: '/api/rentals/users/search',
			requestContentType: 'application/json',
			body: [
				email: '@rentals.io'
				]
		);
		
		println result.data;
	}
	
	// Makes a POST request to add a new user, the HTTP response
	// will have no body but a Location header with the URL for
	// the newly created user
	private int post_addUser(String accessToken) {
		addHeadersForAccessToken(accessToken);

		def result = client.post(
			path: '/api/rentals/users',
			requestContentType: 'application/json',
			body: [
				email: 'test1@rentals.io',
				password: 'pass',
				roles: [
					[
						id: 2,
						// beside the ID we need to provide the role name in order for
						// the update to be successful - this is because the Role object
						// on the server has equality defined based on name not Id
						name: 'CLIENT'
					]
				]
			]
		);

		String location = result.headers['Location'];

		println 'result: ' + location;

		String revLocation = location.reverse();
		String id =  revLocation.substring(0, revLocation.indexOf('/')).reverse();
		return id as Integer;
	}

	// Makes a PUT request to update a user's roles and password
	private void put_updateUser(int userId, String accessToken) {
		addHeadersForAccessToken(accessToken);

		def result = client.put(
			path: '/api/rentals/users/' + userId,
			requestContentType: 'application/json',
			body: [
				id: userId,
				email: 'test1@rentals.io',
				password: 'pass1',
				roles: [
					[
						id: 2,
						// beside the ID we need to provide the role name in order for
						// the update to be successful - this is because the Role object
						// on the server has equality defined based on name not Id
						name: 'CLIENT'
					],
					[
						id: 1,
						name: 'CLERK'
					]
				]
			]
		);

		println 'result: ' + result.data;
	}
	
	// Makes a DELETE request to delete a user
	private void delete_deleteUser(int userId, String accessToken) {
		addHeadersForAccessToken(accessToken);

		client.headers['Content-Type'] = 'application/json';
		def result = client.delete(
			path: '/api/rentals/users/' + userId,
			requestContentType: 'application/json'
		);
	}
	
	
	// Makes a GET request to retrieve a user's details
	private void get_getUser(int userId, String accessToken) {
		addHeadersForAccessToken(accessToken);

		client.headers['Content-Type'] = 'application/json';
		def result = client.get(
			path: '/api/rentals/users/' + userId,
			requestContentType: 'application/json'
		);
		
		println 'result: ' + result.data;
	}
	
	public void runAll() {
		def accessToken = getAccessToken("admin@rentals.io");
		post_searchUsers(accessToken);
		def userId = post_addUser(accessToken);
		get_getUser(userId, accessToken);
		put_updateUser(userId, accessToken);
		delete_deleteUser(userId, accessToken);
	}
	
	public static void main(String[] args){
		def inst = new RestClientRentals();
		inst.runAll();
	}
}
