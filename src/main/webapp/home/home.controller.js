'use strict';	
	// Controller for View view
angular
        .module('app').controller('abViewCtrl', function ABViewCtrl( $scope, $window, tasksSvc, AuthenticationService,usersSvc)
{
    
    $scope.username = usersSvc.getUser();
     $scope.column  = 'name';
    $scope.reverse = false;

    $scope.exit = function () {
        AuthenticationService.ClearCredentials();
        $window.location.href = "#/login";
    };
	
	$scope.profile = function () {
        $window.location.href = "#/profile";
    };

    // Get addressability for the Tasks data via a data service

     // Define start for album list display and the (max) number to display
    $scope.start = 1;
    $scope.count = 5;

    tasksSvc.all().success(function (data) {
        $scope.Tasks = data;
        tasksSvc.Tasks = $scope.Tasks;
        $scope.total = tasksSvc.Tasks.length;
    });

    $scope.tasksSvc = tasksSvc;

    $scope.showcolor = function (priority) {
		
	var arrcolor = ['danger', 'warning', 'info', 'success', 'active'];
	
	if (priority > 0) return arrcolor[priority-1]; 	
	return "info";	

	};

    // Set the selected Task Id and navigate to the Edit page
    $scope.setTaskId = function (id) {
        // Tell the service to store the Id value and prepare edit data
        $scope.tasksSvc.setTaskId(id);

        // Navigate to the Edit page using the Angular version of window
        $window.location.href = "#/Edit";
    };

    // Add a new Task
    $scope.addTask = function () {
        // Tell the service to reset the selected Task values
        $scope.tasksSvc.clearTask();

        // Navigate to the Add page using the Angular version of window
        $window.location.href = "#/Add";
    };

    // Delete an Task
    $scope.deleteTask = function (id, name) {
        if (confirm('Are you sure you want to delete "' + name + '"?'))
        {
            tasksSvc.deleteTask(id).success(function (data) {

                // Tell the service to delete the specified Task from the data model
                //$scope.tasksSvc.deleteTask(Id);
                tasksSvc.selectedId = id;

                // We must find the index of the Task to be deleted
                var index = tasksSvc.getIndex();

                // Remove it!
                tasksSvc.Tasks.splice(index, 1);

                $scope.Tasks = $scope.tasksSvc.Tasks;
                $scope.total = $scope.Tasks.length;
            });
        }
    };

});

// Controller for Edit view
angular
        .module('app').controller('abEditCtrl', function ABEditCtrl($scope, $window, tasksSvc)
{
    $scope.changed = false;

    // Save the Task changes and return to the View page
    $scope.save = function () {
        // Tell the service to save the Task changes
        //   $scope.tasksSvc.save();

        if (tasksSvc.selectedId == -1) {
            var maxId = -1;

            for (var I = 0; I < tasksSvc.Tasks.length; I++) {
                if (tasksSvc.Tasks[I].id > maxId)
                    maxId = tasksSvc.Tasks[I].id;
            }

            maxId++;
            $scope.newtask = {id: maxId, name: tasksSvc.selectedName,
            done: tasksSvc.selectedDone, start: tasksSvc.selectedStart,
			finish: tasksSvc.selectedFinish, description: tasksSvc.selectedDescr, priority: tasksSvc.selectedPrior};

            tasksSvc.createTask($scope.newtask).success(function (data) {
                //tasksSvc.Tasks.push($scope.newtask);
                tasksSvc.Tasks.push(data);
                $scope.newtask = null;
				$scope.total = tasksSvc.Tasks.length;
				});

            // Add the new Task element to the Tasks array

        }
        // Otherwise we update an existing Task
        else {
            
            $scope.activetask = {id: tasksSvc.selectedId, name: tasksSvc.selectedName,
            done: tasksSvc.selectedDone, start: tasksSvc.selectedStart,
			finish: tasksSvc.selectedFinish, description: tasksSvc.selectedDescr, priority: tasksSvc.selectedPrior};

            tasksSvc.updateTask($scope.activetask).success(function (data) {
                var index0 = tasksSvc.getIndex();
                    tasksSvc.Tasks[index0]=data;
            });



        }
        // And return to the Tasks view page
        $window.location.href = "#/View";
    }
    // Cancel Task changes
    $scope.cancel = function () {
        if (confirm('Return to view without saving?'))
            $window.location.href = "#/View";
    }
});