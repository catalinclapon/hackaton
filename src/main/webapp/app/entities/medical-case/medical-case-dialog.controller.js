(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('MedicalCaseDialogController', MedicalCaseDialogController);

    MedicalCaseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MedicalCase', 'Patient'];

    function MedicalCaseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MedicalCase, Patient) {
        var vm = this;

        vm.medicalCase = entity;
        vm.clear = clear;
        vm.save = save;
        vm.patients = Patient.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.medicalCase.id !== null) {
                MedicalCase.update(vm.medicalCase, onSaveSuccess, onSaveError);
            } else {
                MedicalCase.save(vm.medicalCase, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackatonApp:medicalCaseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
