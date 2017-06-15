(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('RegistryDialogController', RegistryDialogController);

    RegistryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Registry'];

    function RegistryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Registry) {
        var vm = this;

        vm.registry = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.registry.id !== null) {
                Registry.update(vm.registry, onSaveSuccess, onSaveError);
            } else {
                Registry.save(vm.registry, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackatonApp:registryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
