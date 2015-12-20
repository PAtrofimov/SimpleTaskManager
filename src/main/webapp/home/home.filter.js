'use strict';
angular.module('app').filter('checkmark', function () {
    return function (input) {
        return input ? '\u2713' : '\u2718';
    };
});

angular.module('app').filter('split', function() {
    return function(tasks, start, count) {
		
        var filteredTasks = new Array();
        if (angular.isUndefined(tasks) === false ){
        // Arrays start at 0 and start stats at 1. So we subtract 1.
        filteredTasks = tasks.slice(start-1, start-1 + count);
        }
        return filteredTasks;
    } 
});


//angular.module('app').filter('range', function() {
//  return function(input, total) {
//    total = parseInt(total);
//    
//    // totalRange = Math.ceil(total/5) *5;
//
//    for (var i=5; i<total+6; i=i+5) {
//      input.push(i);
//    }
//
//    return input;
//  };
//});
