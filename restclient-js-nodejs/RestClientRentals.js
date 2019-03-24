
var req = require('request');

var clientId = 'rentals-client-id';
var clientSecret = 'ddkl44#SDsfg';

var credentials = clientId + ':' + clientSecret;

/*
 * Retrieves an OAuth2 access token given a user's email
 */
function getAccessToken(email, callback) {
	
	const options =  {
			method: 'POST',
		    uri: 'http://localhost:8080/oauth/token',
		    form: {
		    	grant_type: 'password',
			    username: email,
			    password: 'pass',
			    scope: 'read write'
		    },
		    headers: {
		    	'Authorization': 'Basic ' + Buffer.from(credentials).toString('base64'),
	    		'Content-Type': 'application/x-www-form-urlencoded'
		    },
		    // parse JSON and convert into JS object
		    json: true
		};
	
	req(options, (error, response, body) => {
		if(error) {
			callback(error)
		} else if(response.statusCode != 200) {
			callback(new Error('Response status is ' + response.statusCode + '. Body is ' 
					+ JSON.stringify(body)));
		} else {
			callback(error, body.access_token)
		}
	})
}

/*
 * Makes a POST request to search for all users with an email 
 * address @rentals.io
 */
function post_searchUsers(token, callback) {
	const options =  {
			method: 'POST',
		    uri: 'http://localhost:8080/api/rentals/users/search',
		    body: {
		    	email: '@rentals.io'
		    },
		    headers: {
		    	'Authorization': 'Bearer ' + token,
	    		'Content-Type': 'application/json'
		    },
		    // parse JSON and convert into JS object
		    json: true
		};
	
	req(options, (error, response, body) => {
		if(error) {
			callback(error)
		} else if(response.statusCode != 200) {
			callback(new Error('Response status is ' + response.statusCode + '. Body is ' 
					+ JSON.stringify(body)));
		} else {
			callback(error, body)
		}
	})
}

/*
 * Makes a POST request to add a user
 */
function post_addUser(token, callback) {
	const options =  {
			method: 'POST',
		    uri: 'http://localhost:8080/api/rentals/users',
		    body: {
		    	email:"test1@rentals.com", 
                password:"pass", 
                roles: [{
                     id: 2, 
                     name:"CLIENT"
                 }]
		    },
		    headers: {
		    	'Authorization': 'Bearer ' + token,
	    		'Content-Type': 'application/json'
		    },
		    // parse JSON and convert into JS object
		    json: true
		};
	
	req(options, (error, response, body) => {
		if(error) {
			callback(error)
		} else if(response.statusCode != 201) {
			callback(new Error('Response status is ' + response.statusCode + '. Body is ' 
					+ JSON.stringify(body)));
		} else {
			// extract userId from the Location header
			var locArr = response.headers.location.split('/');		
			callback(error, locArr[locArr.length - 1]);
		}
	})
}

/*
 * Makes a PUT request to update a user
 */
function put_updateUser(token, userId, callback) {
	const options =  {
			method: 'PUT',
		    uri: 'http://localhost:8080/api/rentals/users/' + userId,
		    body: {
		    	email:"test1@rentals.com", 
                password:"pass1", 
                roles: [{
                     id: 2, 
                     name:"CLIENT"
                 },
                 {
                     id: 1, 
                     name:"CLERK"
                 }]
		    },
		    headers: {
		    	'Authorization': 'Bearer ' + token,
	    		'Content-Type': 'application/json'
		    },
		    // parse JSON and convert into JS object
		    json: true
		};
	
	req(options, (error, response, body) => {
		if(error) {
			callback(error)
		} else if(response.statusCode != 204) {
			callback(new Error('Response status is ' + response.statusCode + '. Body is ' 
					+ JSON.stringify(body)));
		} else {		
			callback(error);
		}
	})
}

/*
 * Makes a DELETE request to delete a user
 */
function delete_deleteUser(token, userId, callback) {
	const options =  {
			method: 'DELETE',
		    uri: 'http://localhost:8080/api/rentals/users/' + userId,
		    headers: {
		    	'Authorization': 'Bearer ' + token,
		    	'Content-Type': 'application/json'
		    },
		    // parse JSON and convert into JS object
		    json: true
		};
	
	req(options, (error, response, body) => {
		if(error) {
			callback(error)
		} else if(response.statusCode != 204) {
			callback(new Error('Response status is ' + response.statusCode + '. Body is ' 
					+ JSON.stringify(body)));
		} else {		
			callback(error);
		}
	})
}

/*
 * Makes a GET request to retrieve a user
 */
function get_getUser(token, userId, callback) {
	const options =  {
			method: 'GET',
		    uri: 'http://localhost:8080/api/rentals/users/' + userId,
		    headers: {
		    	'Authorization': 'Bearer ' + token,
		    	'Content-Type': 'application/json'
		    },
		    // parse JSON and convert into JS object
		    json: true
		};
	
	req(options, (error, response, body) => {
		if(error) {
			callback(error)
		} else if(response.statusCode != 200) {
			callback(new Error('Response status is ' + response.statusCode + '. Body is ' 
					+ JSON.stringify(body)));
		} else {		
			callback(error, body);
		}
	})
}

/*
 * Run all methods covered in the post.
 */
function runAll(token) {
	// search for users first
	post_searchUsers(token, (error, body) => {
		if(error) {
			console.log(error);
		} else {
			console.log(body);
			
			// add a user now
			post_addUser(token, (error, userId) => {
				if(error) {
					console.log(error);
				} else {
					console.log(userId);
					
					// update the password for the newly added user
					put_updateUser(token, userId, (error) => {
						if(error) {
							console.log(error);
						} else {
							console.log('User ' + userId + ' updated successfuly')
							
							// get the user now
							get_getUser(token, userId, (error, body) => {
								if(error) {
									console.log(error);
								} else {
									console.log('User : ' + userId + ': ' + JSON.stringify(body));
									
									// delete the user now
									delete_deleteUser(token, userId, (error) => {
										if(error) {
											console.log(error);
										} else {
											console.log('User ' + userId + ' deleted successfuly');
										}
									})
								}
							})
						}
					})
				}
			})
		}
	});
	
}

/*
 * Get an access token first and use it for all methods
 */
getAccessToken('admin@rentals.io', (err, token) => {
	if(err) {
		console.log(err);
	} else {
		runAll(token);
	}
});

