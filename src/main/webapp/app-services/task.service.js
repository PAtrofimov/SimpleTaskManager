'use strict';

// Service object for holding all the main data model for building the web page
angular
        .module('app').service('tasksSvc', function ($http) {

    this.Tasks;
    this.selectedName;
    this.selectedDone;
    this.selectedStart;
    this.selectedFinish;
	this.selectedPrior;
    this.selectedDescr;
    var BASE_URL = "/View";



    this.all = function () {
        return $http.get(BASE_URL + "/?event=managertask.handlers.SelectTasksHandler");
    };
    this.createTask = function (task) {
        return $http.post(BASE_URL + "/?event=managertask.handlers.InsertTaskHandler", task);
    };
    this.updateTask = function (task) {
        return $http.put(BASE_URL + "/?event=managertask.handlers.UpdateTaskHandler", task);
    };
    this.deleteTask = function (id) {
        return $http.delete(BASE_URL + "/?event=managertask.handlers.DeleteTaskHandler&id=" + id);
    };



    this.selectedId;

    this.getIndex = function () {
        var index = -1;

        for (var I = 0; I < this.Tasks.length; I++)
        {
            if (this.Tasks[I].id == this.selectedId)
                index = I;
        }
        return index;
    }

    // View sets the selected Task index - this copies the selected Task
    // data values into separate data fields that then appear in the Edit page.
    this.setTaskId = function (id) {
        this.selectedId = id;

        // We must find the index of the item to be saved
        var index = this.getIndex();

        this.selectedName = this.Tasks[index].name;
        this.selectedDone = this.Tasks[index].done;
        this.selectedStart = new Date(this.Tasks[index].start);
        this.selectedFinish = new Date(this.Tasks[index].finish);
		this.selectedPrior = this.Tasks[index].priority;
        this.selectedDescr = this.Tasks[index].description;
        

    }

    // Clear the selected Task values in preparation for adding a new one 
    this.clearTask = function () {
        this.selectedId = -1;
        this.selectedName = "";
        this.selectedDone = false;
		this.selectedPrior = 1;
        this.selectedDescr = "";
        
        var cur_date = new Date();
        cur_date.setHours(0);
        cur_date.setMinutes(0);
        cur_date.setSeconds(0);
        this.selectedStart = cur_date;
        this.selectedFinish = cur_date;

    }

});
