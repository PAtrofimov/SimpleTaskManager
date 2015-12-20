'use strict';

angular
        .module('app').directive('abTask', function ABTask() {
    return {
        // We limit this directive to use as an attribute only.
        // Note that 'A' is the default, so not strictly needed here.
        restrict: 'A',
        // We do not want to use the contents of the element
        transclude: false,
        // We pick up the element title for display
        scope: {
            name: "@",
            made: "@",
            done: "@",
            id: "@",
            start: "@",
            finish: "@",
			description: "@",
			priority: "@"
        },
        // We format the Task fields
        templateUrl: 'Task.html'
    }
});

angular
        .module('app').directive('abTaskEdit', function (tasksSvc) {
    return {
        link: function (scope) {
            scope.tasksSvc = tasksSvc;
        },
        // We limit this directive to use as an attribute only.
        // Note that 'A' is the default, so not strictly needed here.
        restrict: 'A',
        // We need this to establish access to the save and cancel functions
        controller: 'abEditCtrl',
        // We do not want to use the contents of the element
        transclude: false,
        // We pick up the element title for display
        scope: {
            label: "@"
        },
        // We format the Task fields
        templateUrl: 'TaskEdit.html'
    }
});

