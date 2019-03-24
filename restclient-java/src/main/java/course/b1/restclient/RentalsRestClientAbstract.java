package course.b1.restclient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.hobsoft.spring.resttemplatelogger.LoggingCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class RentalsRestClientAbstract {

	// Client ID and secret that identifies this client 
	// for the REST API
	private static final String clientId = "rentals-client-id";
	private static final String clientSecret = "ddkl44#SDsfg";
	
	// RestTemplate instance to be used for making calls to 
	// the REST API 
	@Autowired
	protected RestTemplate restTemplate;

	
	protected RentalsRestClientAbstract() {
		super();
	}

	/**
	 * Obtains an access token for the given username. This access token
	 * is used for subsequent requests to the RESTful API.
	 * @param email the email for which to obtain an access token
	 * @return an access token
	 * @throws Exception
	 */
	protected String getAccessToken(String email) throws Exception {
		// we are building here an HTTP POST request for URL /oauth/token
		// once we pass the required OAuth2 parameters we get back the access token
		// used for subsequent invocations of the REST API
		
		// build the request headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		// add basic authentication with clientid and clientsecret
		String credentials = clientId + ":" + clientSecret;
		String encodedCredentials = Base64.getEncoder().encodeToString(
				credentials.getBytes(StandardCharsets.UTF_8));
		headers.add("Authorization", "Basic " + encodedCredentials);
		
		// build the request body using the set of required OAuth2 parameters
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("username", email);
		params.add("password", "pass");
		params.add("scope", "read write");
		
		// build the request using the parameters and the headers created earlier
		HttpEntity<MultiValueMap<String, String>> request = 
				new HttpEntity<MultiValueMap<String, String>>(params, headers);
		
		// execute the request against /oauth/token
		String result = restTemplate
			.postForEntity(createURL("/oauth/token"), request, String.class).getBody();
		
		// extract the access token from the result
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(result).get("access_token").toString();
	}

	/**
	 * Obtains an access token and then builds <code>HttpHeaders</code> with
	 * an Authorization Bearer header containing the access token. The returned headers
	 * are used to build the HTTP requests
	 * @param email
	 * @return
	 * @throws Exception
	 */
	protected HttpHeaders createHeadersForUser(String email) throws Exception {
		return createHeadersForAccessToken(getAccessToken(email));
	}

	/**
	 * Builds <code>HttpHeaders</code> with an Authorization Bearer header containing 
	 * the access token. The returned headers are used to build the HTTP requests
	 * @param email
	 * @return
	 * @throws Exception
	 */
	protected HttpHeaders createHeadersForAccessToken(String accessToken) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + accessToken);
		return headers;
	}

	protected String createURL(String path) {
		return "http://localhost:8080" + path;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplateBuilder()
			    .customizers(new LoggingCustomizer())
			    .build();
	}
}