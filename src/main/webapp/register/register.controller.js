'use strict';
 angular
        .module('app')
        .controller('RegisterController', RegisterController);
 
    RegisterController.$inject = ['usersSvc', '$location', '$rootScope', '$scope','FlashService'];
    function RegisterController(usersSvc, $location, $rootScope,$scope, FlashService) {
       // var vm = this;
 
        $scope.register = register;
 
        function register() {
            $scope.dataLoading = true;
            usersSvc.Create($scope.user)
                .then(function (response) {
                    if (response.success) {
                        FlashService.Success('Registration successful', true);
                        $location.path('/login');
                    } else {
                        FlashService.Error(response.message);
                        $scope.dataLoading = false;
                    }
                });
        }
    };