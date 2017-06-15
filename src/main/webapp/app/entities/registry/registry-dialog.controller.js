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

    RegistryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Registry'];

    function RegistryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Registry) {
        var vm = this, groupCount = 0;

        vm.registry = entity;
        vm.clear = clear;
        vm.save = save;
        vm.removeField = removeField;
        vm.removeCategory = removeCategory;
        vm.newField = [];
        vm.groups = [];
        vm.groupedFields = groupByKey();
        vm.newGroup = newGroup;
        vm.addNewField = addNewField;


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

        function recreateRegistry(){
            var result = {};

            result.id = vm.registry.id;
            result.uuid = vm.registry.uuid;
            result.name = vm.registry.name;
            result.desc = vm.registry.desc;

            result.fields = [];

            vm.groups.forEach(function(item, index) {
                var fields = vm.groupedFields[index];
                if(angular.isDefined(fields)){
                    fields.forEach(function(field, findex){
                        result.fields.push({
                            category: item.name,
                            order: index*10,
                            field: field
                        });
                    });
                }
            });

            return result;
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackatonApp:registryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function removeCategory(index) {
            var cat = vm.groups[index];
            delete vm.groupedFields[cat]
            vm.groups.remove(cat);
        }

        function removeField(field, index) {
            var grouped = vm.groupedFields[index];
            if(angular.isDefined(grouped)){
                grouped.remove(field)
            }
        }

        function newGroup(){
            vm.groups.push({name: 'New group ' + groupCount++});
        }

        function addNewField(index){
            var field = vm.newField[index];

            if(field.name != '') {
                if(angular.isUndefined(vm.groupedFields[index])){
                    vm.groupedFields[index] = [];
                }

                vm.groupedFields[index].push({
                    name: field.name,
                    type: field.type,
                    required: field.required ? 'y' : '',
                    min: field.min,
                    max: field.max,
                    extValidation: field.values
                });
                vm.newField[index] = {
                    name: '',
                    type: '',
                    required: '',
                    min: '',
                    max: '',
                    values: ''
                };
            }
        }

        function groupByKey(){
            var grouped = [], fields = entity.fields, index;

            for(var i = 0; i < fields.length; i++){
                index = getCategoryIndex(fields[i].category);
                if(index == -1){
                    vm.groups.push({name: fields[i].category});
                    index = getCategoryIndex(fields[i].category);
                    vm.newField[index] = {
                        name: '',
                        type: '',
                        required: '',
                        min: '',
                        max: '',
                        values: ''
                    };
                    grouped[index] = [];
                    groupCount++;
                }
                grouped[index].push(fields[i].field);
            }

            return grouped;
        }

        function getCategoryIndex(category) {
            for(var i = 0; i < vm.groups.length; i++) {
                if(vm.groups[i].name == category) {
                    return i;
                }
            }
            return -1;
        }
    }
})();
