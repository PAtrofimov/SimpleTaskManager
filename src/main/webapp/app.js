'use strict';

angular.module('app', ['ngRoute', 'ngCookies']).config(['$routeProvider',function ($routeProvider) {
        $routeProvider
                .when('/View', {
                    templateUrl: 'home/home.view.html',
                    controller: 'abViewCtrl'
                })
                .when('/', {
                    templateUrl: 'home/home.view.html',
                    controller: 'abViewCtrl'
                })
                .when('/Edit', {
                    templateUrl: 'home/home.edit.html',
                    controller: 'abEditCtrl'
                })
                .when('/Add', {
                    templateUrl: 'home/home.add.html',
                    controller: 'abEditCtrl'
                })
                .when('/login', {
                    controller: 'LoginController',
                    templateUrl: 'login/login.view.html'
                })
				.when('/profile', {
                    controller: 'ProfileController',
                    templateUrl: 'profile/profile.view.html'
                })
				.when('/admin', {
                    controller: 'AdminController',
                    templateUrl: 'admin/admin.view.html'
                })
				.when('/register', {
                controller: 'RegisterController',
                templateUrl: 'register/register.view.html'

            })

                .otherwise({
                    redirectTo: '/login'
                });
    }])

        .run(['$rootScope', '$location', '$cookieStore', '$http',
            function ($rootScope, $location, $cookieStore, $http) {
                // keep user logged in after page refresh
                $rootScope.globals = $cookieStore.get('globals') || {};
                if ($rootScope.globals.currentUser) {
                    $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
                }

                $rootScope.$on('$locationChangeStart', function (event, next, current) {
                    // redirect to login page if not logged in and trying to access a restricted page
				var arr = ['/login', '/register'];
                var restrictedPage=arr.indexOf($location.path())=== -1;    	
            
				var loggedIn = $rootScope.globals.currentUser;
				
				if (restrictedPage && !loggedIn) {
                $location.path('/login');
				}
				
				if  (loggedIn && $location.path() == '') {
					if (loggedIn.admin == 'true') {$location.path('/admin');}
     			}
                                
//                                if ($location.path() == '/admin' && loggedIn) {
//                $location.path('/login');
//				}
                                
                });
            }]);
