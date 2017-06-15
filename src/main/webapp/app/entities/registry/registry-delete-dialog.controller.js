(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('RegistryDeleteController',RegistryDeleteController);

    RegistryDeleteController.$inject = ['$uibModalInstance', 'entity', 'Registry'];

    function RegistryDeleteController($uibModalInstance, entity, Registry) {
        var vm = this;

        vm.registry = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Registry.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
