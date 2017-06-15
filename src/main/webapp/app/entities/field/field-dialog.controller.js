(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('FieldDialogController', FieldDialogController);

    FieldDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Field'];

    function FieldDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Field) {
        var vm = this;

        vm.field = entity;
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
            if (vm.field.id !== null) {
                Field.update(vm.field, onSaveSuccess, onSaveError);
            } else {
                Field.save(vm.field, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackatonApp:fieldUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
