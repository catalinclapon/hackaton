(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('MedicalCaseDeleteController',MedicalCaseDeleteController);

    MedicalCaseDeleteController.$inject = ['$uibModalInstance', 'entity', 'MedicalCase'];

    function MedicalCaseDeleteController($uibModalInstance, entity, MedicalCase) {
        var vm = this;

        vm.medicalCase = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            MedicalCase.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
