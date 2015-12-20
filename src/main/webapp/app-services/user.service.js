'use strict';

// Service object for holding all the main data model for building the web page
angular
        .module('app').service('usersSvc', function ($rootScope,$http) {

     var service = {};
	 var user = '';
	 
	 var BASE_URL = "/Users";
	 
	 if ($rootScope.globals.currentUser) {
                   user = $rootScope.globals.currentUser.username;
     };

                        service.getUser = function () {
                            return user;
                        };

                        service.setUser = function (value) {
                            user = value;
                        };
						
		service.Create = function Create(user) {
							 
		return $http.post(BASE_URL + "/?event=managertask.handlers.InsertUserHandler", user).then(handleSuccess, handleError('Error creating user'));
          
        };
		
		service.Update = function Update(user) {
							 
		return $http.post(BASE_URL + "/?event=managertask.handlers.UpdateUserHandler", user).then(handleSuccess, handleError('Error updating user'));
         
        };
		
		service.GetByName = function GetByName(name) {
							 
		return $http.post(BASE_URL + "/?event=managertask.handlers.SelectUserHandler", name).then(handleSuccess, handleError('Error getting user'));
         
        };
		
		service.GetAll = function GetAll() {
            return $http.get(BASE_URL + "/?event=managertask.handlers.SelectAllUsersHandler").then(handleSuccess, handleError('Error getting all users'));
        }
       
		
		service.Delete = function Delete(id) {
            return $http.delete(BASE_URL + "/?event=managertask.handlers.DeleteUserHandler&id=" + id).then(handleSuccess, handleError('Error deleting user'));
        }
		
						
		// private functions

        function handleSuccess(res) {
            return res.data;
        }
		
		
        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }				
						
						return service;
	 
		});