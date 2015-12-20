    'use strict';

    angular
        .module('app')
        .controller('AdminController', AdminController);

    AdminController.$inject = ['usersSvc', 'tasksSvc', 'Pagination', '$rootScope', '$scope'];
    function AdminController(usersSvc, tasksSvc, Pagination, $rootScope, $scope) {
   //     var vm = this;
        $scope.paginationUser = Pagination.getNew(10);
        $scope.paginationTask = Pagination.getNew(10);
        $scope.user = null;
        $scope.task = null;
        $scope.allUsers = [];
        $scope.allTasks = [];
        $scope.deleteUser = deleteUser;
        $scope.deleteTask = deleteTask;
        $scope.totalUsers=0;
        $scope.totalTasks=0;
        initController();

        function initController() {
            loadCurrentUser();
            loadAllUsers();
            loadAllTasks();
        }

        function loadCurrentUser() {
            usersSvc.GetByName($rootScope.globals.currentUser.username)
                .then(function (user) {
                    $scope.user = user;
                });
        }

        function loadAllUsers() {
            usersSvc.GetAll()
                .then(function (users) {
                    $scope.allUsers = users;
                    $scope.totalUsers = $scope.allUsers.length;
                    $scope.paginationUser.numPages = Math.ceil($scope.totalUsers/$scope.paginationUser.perPage);
              //      if ($scope.limitUser == "") {$scope.limitUser =$scope.range[0] };
                });
        }
        
       function loadAllTasks() {
           tasksSvc.all().success(function (tasks) {
                   $scope.allTasks = tasks;
                   $scope.totalTasks = $scope.allTasks.length;
                   $scope.paginationTask.numPages = Math.ceil($scope.totalTasks/$scope.paginationTask.perPage);
               });
       }

        function deleteUser(id) {
            usersSvc.Delete(id)
            .then(function () {
                loadAllUsers();
            });
        }
        
       function deleteTask(id) {
           tasksSvc.deleteTask(id)
           .success(function () {
               loadAllTasks();
           });
       }
    }
