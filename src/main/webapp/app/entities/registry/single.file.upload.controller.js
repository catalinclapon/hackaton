(function () {
    'use strict';
    var app = angular.module('hackatonApp').controller('SingleFileUploadCtrl', ['$scope', 'Upload', '$timeout', function ($scope, Upload, $timeout) {
        $scope.uploadFile = function (file) {
            $scope.file = file;
            file.upload = Upload.upload({
                url: 'api/upload',
                data: {file: file}
            });

            file.upload.then(function (response) {
                $timeout(function () {
                    file.result = response.data;
                });
            }, function (response) {

            }, function (evt) {
                file.progress = Math.min(100, parseInt(100.0 *
                    evt.loaded / evt.total));
            });

        }
    }]);
})();
