(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('MedicalCaseFieldDeleteController',MedicalCaseFieldDeleteController);

    MedicalCaseFieldDeleteController.$inject = ['$uibModalInstance', 'entity', 'MedicalCaseField'];

    function MedicalCaseFieldDeleteController($uibModalInstance, entity, MedicalCaseField) {
        var vm = this;

        vm.medicalCaseField = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MedicalCaseField.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
