(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('PatientDeleteController',PatientDeleteController);

    PatientDeleteController.$inject = ['$uibModalInstance', 'entity', 'Patient'];

    function PatientDeleteController($uibModalInstance, entity, Patient) {
        var vm = this;

        vm.patient = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Patient.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
