(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('RegistryFieldDeleteController',RegistryFieldDeleteController);

    RegistryFieldDeleteController.$inject = ['$uibModalInstance', 'entity', 'RegistryField'];

    function RegistryFieldDeleteController($uibModalInstance, entity, RegistryField) {
        var vm = this;

        vm.registryField = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RegistryField.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
