(function () {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('MedicalCaseDialogController', MedicalCaseDialogController);

    MedicalCaseDialogController.$inject = ['$filter', '$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'registry', 'MedicalCase', 'Patient', 'Upload'];

    function MedicalCaseDialogController($filter, $timeout, $scope, $stateParams, $uibModalInstance, entity, registry, MedicalCase, Patient, Upload) {
        var vm = this;

        vm.medicalCase = entity;
        vm.registry = registry;
        vm.fields = [];
        vm.values = [];
        vm.clear = clear;
        vm.save = save;
        //vm.patients = Patient.query();
        vm.patientCnp = $stateParams.cnp;
        vm.name = vm.medicalCase.name;

        vm.files = [];
        vm.errFiles = [];

        if (entity.fields !== undefined) {
            entity.fields.forEach(function (item, index) {
                vm.values['' + item.field.id] = item.value;
            });
        }

        registry.fields.forEach(function (item, index) {
            vm.fields.push({
                id: item.field.id,
                category: item.category,
                type: item.field.type,
                order: item.order,
                name: item.field.name,
                values: angular.isDefined(item.field.extValidation) && item.field.extValidation ? item.field.extValidation.split(',') : [],
                value: vm.values['' + item.field.id]
            });
        });

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            var medicalCase = recreateMedicalCase();
            // if (vm.medicalCase.uuid !== null) {
            //     MedicalCase.update(vm.medicalCase, onSaveSuccess, onSaveError);
            // } else {
            //     MedicalCase.save(medicalCase, onSaveSuccess, onSaveError);
            // }

            MedicalCase.save(medicalCase, onSaveSuccess, onSaveError);

            // TODO while uploading the files,
            //      - pass the id of the medical case
            //      - if we get errors while saving/updating don't proceed with the file uplaod

            angular.forEach(vm.files, function (file) {
                file.upload = Upload.upload({
                    url: 'api/upload',
                    data: {file: file}
                });

                file.upload.then(function (response) {
                    $timeout(function () {
                        file.result = response.data;
                    });
                }, function (response) {
                    if (response.status > 0)
                        $scope.errorMsg = response.status + ': ' + response.data;
                }, function (evt) {
                    file.progress = Math.min(100, parseInt(100.0 *
                    evt.loaded / evt.total));
                });
            });
        }

        vm.uploadFiles = function(files, errFiles) {
            $scope.files = files;
            $scope.errFiles = errFiles;
            angular.forEach(files, function (file) {
                file.upload = Upload.upload({
                    url: 'api/upload',
                    data: {file: file}
                });

                file.upload.then(function (response) {
                    $timeout(function () {
                        file.result = response.data;
                    });
                }, function (response) {
                    if (response.status > 0)
                        $scope.errorMsg = response.status + ': ' + response.data;
                }, function (evt) {
                    file.progress = Math.min(100, parseInt(100.0 *
                        evt.loaded / evt.total));
                });
            });
        };

        function recreateMedicalCase() {
            var result = {};
            result.name = vm.name;
            result.registryUuid = vm.registry.uuid;

            result.patientCnp = vm.patientCnp;

            result.fields = [];

            vm.fields.forEach(function (item, index) {
                    result.fields.push({
                        id: item.id,
                        field: item,
                        value: item.value
                    });
                }
            );

            result.attachments = [];

            $scope.files.forEach((function (item) {
                if (item.result !== undefined && item.result.id !== undefined) {
                    result.attachments.push({id: item.result.id});
                }
            }));

            return result;
        }

        function onSaveSuccess(result) {
            $scope.$emit('hackatonApp:medicalCaseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        function onChangeDate(field) {
            //$filter('date')(field.value, dateFormat);
        }

        function onSelectFiles(files, errFiles) {
            vm.files = files;
            vm.errFiles = errFiles;
        }

        $scope.uploadFiles = function (files, errFiles) {
            vm.files = files;
            vm.errFiles = errFiles;

            // TODO update dialog to show the name of the files that will be uplaoded
        }
    }
})();
