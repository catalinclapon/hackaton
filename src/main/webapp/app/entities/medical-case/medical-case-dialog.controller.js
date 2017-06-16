(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('MedicalCaseDialogController', MedicalCaseDialogController);

    MedicalCaseDialogController.$inject = ['$filter', '$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'registry', 'MedicalCase', 'Patient'];

    function MedicalCaseDialogController ($filter, $timeout, $scope, $stateParams, $uibModalInstance, entity, registry, MedicalCase, Patient) {
        var vm = this;

        vm.medicalCase = entity;
        vm.registry = registry;
        vm.fields = [];
        vm.clear = clear;
        vm.save = save;
        vm.patients = Patient.query();

        registry.fields.forEach(function(item, index) {
            vm.fields.push({
                id: item.field.id,
                category: item.category,
                type: item.field.type,
                order: item.order,
                name: item.field.name,
                values: angular.isDefined(item.field.extValidation) && item.field.extValidation ? item.field.extValidation.split(',') : []
            });
        });

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

        function onChangeDate(field) {
            //$filter('date')(field.value, dateFormat);
        }

    }
})();
