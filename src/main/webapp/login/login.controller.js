'use strict';
  
angular.module('app')
  
.controller('LoginController',
    ['$scope', '$rootScope', '$location', 'AuthenticationService','usersSvc',
    function ($scope, $rootScope, $location, AuthenticationService, usersSvc) {
        // reset login status
        AuthenticationService.ClearCredentials();
  
        $scope.login = function () {
            $scope.dataLoading = true;
            AuthenticationService.Login($scope.username, $scope.password, function(response) {
                if(response.success) {
                    AuthenticationService.SetCredentials($scope.username, $scope.password, response.admin);
                    usersSvc.setUser($scope.username);
					if (response.admin ==="true") { $location.path('/admin') } else
						{$location.path('/')}
                } else {
                    $scope.error = response.message;
                    $scope.dataLoading = false;
                }
            });
        };
                
    }]);