(function () {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('BulkUploadController', BulkUploadController);

    BulkUploadController.$inject = ['$uibModalInstance', 'entity', 'Registry'];

    function BulkUploadController($uibModalInstance, entity, Registry) {
        var vm = this;

        vm.registry = entity;
        vm.clear = clear;
        vm.bulkUpload = bulkUpload;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function bulkUpload() {

            /*get the file and send it to post*/
            $uibModalInstance.close(true);

        }
    }
})();
