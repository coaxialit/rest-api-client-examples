import requests

import base64


class RestClientRentals:
    
   
    def __init__(self):
        ''' 
        Client ID and secret that identifies this client
        for the REST API
        '''
        self.clientId = 'rentals-client-id'
        self.clientSecret = 'ddkl44#SDsfg'
    
        self.urlRoot = 'http://localhost:8080'
    
    
    def getAccessToken(self, email):
        credentials = self.clientId + ':' + self.clientSecret
        headers = {
            'Authorization': 'Basic ' + base64.b64encode(credentials),
            'Content-Type': 'application/x-www-form-urlencoded'}
        url = self.urlRoot + '/oauth/token'
        data = {
            'grant_type': 'password',
            'username': email,
            'password': 'pass',
            'scope': 'read write'}
        request = requests.Request('POST', url, data = data, headers = headers)
        prepRequest = request.prepare()
        
        RestClientRentals.pretty_print_POST(prepRequest)
        
        session = requests.Session()
        response = session.send(prepRequest)
       
        return response.json()['access_token']
        
    @staticmethod
    def addHeaderForAccessToken(headers, accessToken):
        headers['Authorization'] = 'Bearer ' + accessToken
    
    '''
    Makes a POST request to search for all users with an email 
    address @rentals.io
    '''
    def post_searchUsers(self, accessToken):
            
        url = self.urlRoot + '/api/rentals/users/search'
        data = '''{
            "email": "@rentals.io"
        }'''
        headers = {
            'Content-Type': 'application/json'
        }
        RestClientRentals.addHeaderForAccessToken(headers, accessToken)
        request = requests.Request('POST', url, data = data, headers = headers)
        prepRequest = request.prepare()
        
        RestClientRentals.pretty_print_POST(prepRequest)
        
        session = requests.Session()
        response = session.send(prepRequest)
        
        print(str(response.content))
    
    
    '''
    Makes a POST request to add a user
    '''
    def post_addUser(self, accessToken):
            
        url = self.urlRoot + '/api/rentals/users/'
        data = '''{
                    "email":"test1@rentals.com", 
                    "password":"pass", 
                    "roles": [{
                         "id": 2, 
                         "name":"CLIENT"
                     }]
                }'''
        headers = {
            'Content-Type': 'application/json'
        }
        RestClientRentals.addHeaderForAccessToken(headers, accessToken)
        request = requests.Request('POST', url, data = data, headers = headers)
        prepRequest = request.prepare()
        
        RestClientRentals.pretty_print_POST(prepRequest)
        
        session = requests.Session()
        response = session.send(prepRequest)
        
        if response.status_code != 201:
            raise Exception('Failed to add user. Status code: {}'.format(response.status_code))
        
        ''' extract the user id from the Location URL '''
        newUserUrl = response.headers['Location']
        lst = newUserUrl.split('/')
        return lst[len(lst) - 1]
    
    
    '''
    Makes a POST request to add a user
    '''
    def put_updateUser(self, accessToken, userId):
            
        url = self.urlRoot + '/api/rentals/users/' + userId
        data = '''{
                    "email":"test1@rentals.com", 
                    "password":"pass1", 
                    "roles": [
                        {
                         "id": 2, 
                         "name":"CLIENT"
                         },
                         {
                          "id": 1, 
                          "name":"CLERK"
                         }
                    ]
                }'''
        headers = {
            'Content-Type': 'application/json'
        }
        RestClientRentals.addHeaderForAccessToken(headers, accessToken)
        request = requests.Request('PUT', url, data = data, headers = headers)
        prepRequest = request.prepare()
        
        RestClientRentals.pretty_print_POST(prepRequest)
        
        session = requests.Session()
        response = session.send(prepRequest)
        
        if response.status_code != 204:
            raise Exception('Failed to update user. Status code: {}'.format(response.status_code))
        
        
    '''
    Makes a DELETE request to delete a user
    '''
    def delete_deleteUser(self, accessToken, userId):
            
        url = self.urlRoot + '/api/rentals/users/' + userId
        headers = {
            'Content-Type': 'application/json'
        }
        RestClientRentals.addHeaderForAccessToken(headers, accessToken)
        request = requests.Request('DELETE', url, headers = headers)
        prepRequest = request.prepare()
        
        RestClientRentals.pretty_print_POST(prepRequest)
        
        session = requests.Session()
        response = session.send(prepRequest)
        
        if response.status_code != 204:
            raise Exception('Failed to update user. Status code: {}'.format(response.status_code))
        
    '''
    Makes a GET request to retrieve a user
    '''
    def get_getUser(self, accessToken, userId):
            
        url = self.urlRoot + '/api/rentals/users/' + userId
        headers = {
            'Content-Type': 'application/json'
        }
        RestClientRentals.addHeaderForAccessToken(headers, accessToken)
        request = requests.Request('GET', url, headers = headers)
        prepRequest = request.prepare()
        
        RestClientRentals.pretty_print_POST(prepRequest)
        
        session = requests.Session()
        response = session.send(prepRequest)
        
        if response.status_code != 200:
            raise Exception('Failed to update user. Status code: {}'.format(response.status_code))
        
    
    
    
    '''
    https://stackoverflow.com/questions/20658572/python-requests-print-entire-http-request-raw
    '''
    @staticmethod
    def pretty_print_POST(req):
    
        print('{}\n{}\n{}\n\n{}'.format(
            '-----------START-----------',
            req.method + ' ' + req.url,
            '\n'.join('{}: {}'.format(k, v) for k, v in req.headers.items()),
            req.body,
        ))
        
    '''
    Runs all methods covered in this post
    '''
    def runAll(self):
        accessToken = self.getAccessToken('admin@rentals.io')
        self.post_searchUsers(accessToken)
        newUserId = self.post_addUser(accessToken)
        self.get_getUser(accessToken, newUserId)
        self.put_updateUser(accessToken, newUserId)
        self.delete_deleteUser(accessToken, newUserId)


inst = RestClientRentals()
inst.runAll()


