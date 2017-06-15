(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('RegistryFieldDialogController', RegistryFieldDialogController);

    RegistryFieldDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RegistryField', 'Registry', 'Field'];

    function RegistryFieldDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RegistryField, Registry, Field) {
        var vm = this;

        vm.registryField = entity;
        vm.clear = clear;
        vm.save = save;
        vm.registries = Registry.query();
        vm.fields = Field.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.registryField.id !== null) {
                RegistryField.update(vm.registryField, onSaveSuccess, onSaveError);
            } else {
                RegistryField.save(vm.registryField, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackatonApp:registryFieldUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
