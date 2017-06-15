(function () {
    'use strict';
    var app = angular.module('hackatonApp').controller('FileUploadCtrl', ['$scope', 'Upload', '$timeout', function ($scope, Upload, $timeout) {
            $scope.uploadFiles = function (files, errFiles) {
                $scope.files = files;
                $scope.errFiles = errFiles;
                angular.forEach(files, function (file) {
                    file.upload = Upload.upload({
                        url: 'api/upload',
                        data: {file: file}
                    });

                    file.upload.then(function (response) {
                        $timeout(function () {
                            file.result = response.data;
                        });
                    }, function (response) {
                        if (response.status > 0)
                            $scope.errorMsg = response.status + ': ' + response.data;
                    }, function (evt) {
                        file.progress = Math.min(100, parseInt(100.0 *
                            evt.loaded / evt.total));
                    });
                });
            }
        }]);
})();
