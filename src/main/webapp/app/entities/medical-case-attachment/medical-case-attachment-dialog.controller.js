(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('MedicalCaseAttachmentDialogController', MedicalCaseAttachmentDialogController);

    MedicalCaseAttachmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MedicalCaseAttachment', 'MedicalCase'];

    function MedicalCaseAttachmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MedicalCaseAttachment, MedicalCase) {
        var vm = this;

        vm.medicalCaseAttachment = entity;
        vm.clear = clear;
        vm.save = save;
        vm.medicalcases = MedicalCase.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.medicalCaseAttachment.id !== null) {
                MedicalCaseAttachment.update(vm.medicalCaseAttachment, onSaveSuccess, onSaveError);
            } else {
                MedicalCaseAttachment.save(vm.medicalCaseAttachment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackatonApp:medicalCaseAttachmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
