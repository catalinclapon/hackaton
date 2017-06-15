(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('MedicalCaseAttachmentDeleteController',MedicalCaseAttachmentDeleteController);

    MedicalCaseAttachmentDeleteController.$inject = ['$uibModalInstance', 'entity', 'MedicalCaseAttachment'];

    function MedicalCaseAttachmentDeleteController($uibModalInstance, entity, MedicalCaseAttachment) {
        var vm = this;

        vm.medicalCaseAttachment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MedicalCaseAttachment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
