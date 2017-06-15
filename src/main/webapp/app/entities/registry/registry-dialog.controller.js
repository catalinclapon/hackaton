(function() {
    'use strict';

    Array.prototype.remove = function() {
        var what, a = arguments, L = a.length, ax;
        while (L && this.length) {
            what = a[--L];
            while ((ax = this.indexOf(what)) !== -1) {
                this.splice(ax, 1);
            }
        }
        return this;
    };

    angular
        .module('hackatonApp')
        .controller('RegistryDialogController', RegistryDialogController);

    RegistryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Registry', 'Field'];

    function RegistryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Registry, Field) {
        var vm = this, groupCount = 0;

        vm.registry = entity;
        vm.clear = clear;
        vm.save = save;
        vm.removeField = removeField;
        vm.removeCategory = removeCategory;
        vm.newField = {};
        vm.groupedFields = groupByKey();
        vm.newGroup = newGroup;
        vm.addNewField = addNewField;
        vm.fields = Field.queryAll();


        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            var registry = recreateRegistry();
            if (vm.registry.id !== null) {
                Registry.update(registry, onSaveSuccess, onSaveError);
            } else {
                Registry.save(registry, onSaveSuccess, onSaveError);
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

        function removeCategory(category) {
            delete vm.groupedFields[category]
        }

        function removeField(field, category) {
            var grouped = vm.groupedFields[category];
            if(angular.isDefined(grouped)){
                grouped.remove(field)
            }
        }

        function newGroup(){
            vm.groupedFields['New group ' + groupCount++] = [];
        }

        function addNewField(category){
            var field = vm.newField[category];
            vm.groupedFields[category].push({
                name: field.name,
                type: field.type,
                required: field.required,
                min: field.min,
                max: field.max,
                extValidation: field.values
            })
        }

        function groupByKey(){
            var grouped = {}, fields = entity.fields;

            for(var i = 0; i < fields.length; i++){
                if(!angular.isDefined(grouped[fields[i].category])){
                    grouped[fields[i].category] = [];
                    vm.newField[fields[i].category] = {
                        name: '',
                        type: '',
                        required: '',
                        min: '',
                        max: '',
                        values: ''
                    };
                    groupCount++;
                }
                grouped[fields[i].category].push(fields[i].field);
            }

            return grouped;
        }
    }
})();
