(function () {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('BulkUploadController', BulkUploadController);

    BulkUploadController.$inject = ['$uibModalInstance', 'entity', 'Upload', '$timeout', 'UploadBulk'];

    function BulkUploadController($uibModalInstance, entity, Upload, $timeout, UploadBulk) {
        var vm = this;
        vm.file = null;
        vm.result = null;
        vm.registry = entity;
        vm.clear = clear;
        vm.bulkUpload = bulkUpload;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function bulkUpload(filePath) {
            UploadBulk.save(filePath);
            /*get the file and send it to post*/
            $uibModalInstance.close(true);

        }

        vm.uploadFile = function (file) {
            vm.file = file;
            file.upload = Upload.upload({
                url: 'api/uploadBulk',
                data: {file: file}
            });

            file.upload.then(function (response) {
                $timeout(function () {
                    vm.result = response.data;
                });
            }, function (response) {
            }, function (evt) {
                file.progress = Math.min(100, parseInt(100.0 *
                    evt.loaded / evt.total));
            });
        };
    }
})();
