'use strict';
 angular
        .module('app')
        .controller('ProfileController', ProfileController);
 
    ProfileController.$inject = ['usersSvc', '$location', '$rootScope', '$scope','FlashService'];
    function ProfileController(usersSvc, $location, $rootScope,$scope, FlashService) {
       // var vm = this;
 
	   $scope.init = init;
	   
	   function init() {
            //$scope.dataLoading = true;
            usersSvc.GetByName($rootScope.globals.currentUser.username)
                .then(function (data) {
					$scope.user = data;
                   // if (response.success) {
                       // FlashService.Success('Update successful', true);
                      //  $location.path('/View');
                   // } else {
                      //  FlashService.Error(response.message);
					//	$scope.dataLoading = false;
                  //  }
                });
        }
	   
	   init();
	   
        $scope.profile = profile;
      
        function profile() {
            $scope.dataLoading = true;
            usersSvc.Update($scope.user)
                .then(function (response) {
                    if (response.success) {
                        FlashService.Success('Update successful', true);
                if ($rootScope.globals.currentUser.admin == 'true') {$location.path('/admin');} else {
                        $location.path('/View');}
                    } else {
                        FlashService.Error(response.message);
						$scope.dataLoading = false;
                    }
                });
        }
    };