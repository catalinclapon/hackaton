(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('MedicalCaseFieldDialogController', MedicalCaseFieldDialogController);

    MedicalCaseFieldDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MedicalCaseField', 'Field', 'MedicalCase'];

    function MedicalCaseFieldDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MedicalCaseField, Field, MedicalCase) {
        var vm = this;

        vm.medicalCaseField = entity;
        vm.clear = clear;
        vm.save = save;
        vm.fields = Field.query();
        vm.medicalcases = MedicalCase.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.medicalCaseField.id !== null) {
                MedicalCaseField.update(vm.medicalCaseField, onSaveSuccess, onSaveError);
            } else {
                MedicalCaseField.save(vm.medicalCaseField, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackatonApp:medicalCaseFieldUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
