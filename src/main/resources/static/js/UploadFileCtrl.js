// CONTROLLER UPLOAD FILE
mainApp.controller('UploadFileController', function($scope, $http) {
	
	$scope.description = "";
	$scope.file = null;
	$scope.uploadResult = "";
	
	$scope.uploadFile = function() {

		var url = "/uploadSingleFile";

		var data = new FormData();

		data.append("description", $scope.description);
		data.append("file", $scope.file);

		var config = {
			transformRequest : angular.identity,
			transformResponse : angular.identity,
			headers : {
				'Content-Type' : undefined
			}
		}

		$http.post(url, data, config).then(
		// Success
		function(response) {
			$scope.uploadResult = response.data;
		},
		// Error
		function(response) {
			$scope.uploadResult = response.data;
		});
	};

});