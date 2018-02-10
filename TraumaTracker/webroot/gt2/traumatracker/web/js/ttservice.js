var app = angular.module('ttService', []);
function TTServiceAccessObj($http, $q) {
  this.getAllReports = function(){
    var deferred = $q.defer();
    /* getting the most recent 10 reports */
    $http.get('/gt2/traumatracker/api/reports', {params: { "mostRecent": 10 }})
    .then(function ok(response) {
    	deferred.resolve(response.data);
    }, function error(response) {
        deferred.reject(response);
    });
    return deferred.promise;
  };
  this.findReports = function(date,opId){
    var deferred = $q.defer();
    $http.get('/gt2/traumatracker/api/reports',{ params: { "date": date, "opId": opId }})
    .then(function(response) {
      deferred.resolve(response.data);
    }, function error(response) {
        deferred.reject(response);
    });
    return deferred.promise;
  }; 
  this.getVersion = function(){
    var deferred = $q.defer();
    $http.get('/gt2/traumatracker/api/version')
    .then(function(response) {
      deferred.resolve(response.data);
    }, function error(response) {
        deferred.reject(response);
    });
    return deferred.promise;
  }; 
  this.deleteReport = function(report){
    var deferred = $q.defer();
    $http.delete('/gt2/traumatracker/api/reports/' + report._id)
    .then(function(response) {
      deferred.resolve(response.data);
    }, function error(response) {
        deferred.reject(response);
    });
    return deferred.promise;
  }; 
  this.deleteEventInReport = function(report,event){
	var deferred = $q.defer();
	$http.delete('/gt2/traumatracker/api/reports/' + report._id + '/events/' + event.eventId)
	.then(function(response) {
	 deferred.resolve(response.data);
	}, function error(response) {
		deferred.reject(response);
	});
	return deferred.promise;
  }; 
  this.getAllUsers = function(){
    var deferred = $q.defer();
    $http.get('/gt2/traumatracker/api/users')
    .then(function(response) {
      deferred.resolve(response.data);
    }, function error(response) {
        deferred.reject(response);
    });
    return deferred.promise;
  }; 
  this.addUser = function(userToAdd){
    var deferred = $q.defer();
    $http.post('/gt2/traumatracker/api/users',userToAdd)
    	.then(function(response) {
    	deferred.resolve(response.data);
    }, function error(response) {
        deferred.reject(response);
    });
    return deferred.promise;
  }; 
  this.deleteAllReports = function(){
	var deferred = $q.defer();
	$http.delete('/gt2/traumatracker/api/reports')
		.then(function(response) {
	    deferred.resolve(response.data);
	}, function error(response) {
	    deferred.reject(response);
	});
	return deferred.promise;
  };
  this.deleteAllUsers = function(){
	var deferred = $q.defer();
	$http.delete('/gt2/traumatracker/api/users')
		.then(function(response) {
		deferred.resolve(response.data);
	}, function error(response) {
		deferred.reject(response);
	});
	return deferred.promise;
  };
  
  // STATISTIC SERVICE
  this.getStat = function(pathFile) {  
	var deferred = $q.defer();
	$http.get(pathFile)
		.then(function(response) {
		deferred.resolve(response.data);
	}, function error(response) {
		deferred.reject(response);
	});
	return deferred.promise;
  };
}

app.service('TTService', ['$http', '$q', TTServiceAccessObj]);

