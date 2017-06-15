(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('FieldDeleteController',FieldDeleteController);

    FieldDeleteController.$inject = ['$uibModalInstance', 'entity', 'Field'];

    function FieldDeleteController($uibModalInstance, entity, Field) {
        var vm = this;

        vm.field = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Field.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
