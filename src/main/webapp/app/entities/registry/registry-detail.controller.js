(function () {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('RegistryDetailController', RegistryDetailController);

    RegistryDetailController.$inject = ['$scope','MedicalCase', '$rootScope', '$stateParams', 'previousState', 'entity', 'Registry', 'RegistryData', '$translate', '$interval', 'AlertService'];

    function RegistryDetailController($scope,MedicalCase, $rootScope, $stateParams, previousState, entity, Registry, RegistryData, $translate, $interval, AlertService) {
        var vm = this, fields = entity.fields, fieldIds = [];

        vm.changeStatus=changeStatus;
        vm.registry = entity;
        vm.medicalCase=entity;
        vm.getSpecificMedicalCases = [];
        vm.previousState = previousState.name;
        vm.selectedCnp = '';
        vm.medicalCases=[];

        var unsubscribe = $rootScope.$on('hackatonApp:registryUpdate', function (event, result) {
            vm.registry = result;
        });

        var columnDefs = [{
            name: 'CNP', enableHiding: false
        }, {
            name: 'Name'
        }];

        getMedicalCases();

        function getMedicalCases() {
            RegistryData.query({
                                id: entity.id,
                                uuid: entity.uuid,
                                fields: fieldIds

                    }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.getSpecificMedicalCases = data;

              /*  vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.registries = data;
                vm.page = pagingParams.page;*/
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

     // change status function
		function changeStatus(medicalCase, status) {
			medicalCase.status = status;
			medicalCase.id = vm.getSpecificMedicalCases[0].id;
			MedicalCase.update(medicalCase);
		}

        if (angular.isDefined(entity.fields)) {

            // fields.sort(function (a, b) {
            //     return (a.category > b.category) ? 1 : ((b.category > a.category) ? -1 :
            //         ( (a.order > b.order) ? 1 : ((b.order > a.order) ? -1 : 0) ) );
            // });
            fields.forEach(function (item, index) {
                columnDefs.push({
                    name: item.field.name
                });
                fieldIds.push(item.field.id);
            });
        }


        $scope.gridOptions = {
            exporterMenuCsv: false,
            enableGridMenu: true,
            gridMenuTitleFilter: $translate,
            columnDefs: columnDefs,
            //gridMenuCustomItems: [
            //    {
            //        title: entity.name,
            //        action: function ($event) {
            //            this.grid.element.toggleClass('rotated');
            //        },
            //        order: 210
            //    }
            //],
            data: RegistryData.query({id: entity.id, uuid: entity.uuid, fields: fieldIds}),
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
                console.log('grid menu');
                // interval of zero just to allow the directive to have initialized
                //$interval( function() {
                //    gridApi.core.addToGridMenu( gridApi.grid, [{ title: 'Dynamic item', order: 100}]);
                //}, 0, 1);

                gridApi.core.on.columnVisibilityChanged($scope, function (changedColumn) {
                    $scope.columnChanged = {name: changedColumn.colDef.name, visible: changedColumn.colDef.visible};

                });
            }
        };

        $scope.gridOptions.onRegisterApi = function (gridApi) {
            //set gridApi on scope
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                var msg = 'row selected ' + row.isSelected;
                if (row.isSelected) {
                    vm.selectedCnp = row.entity.CNP;
                } else {
                    vm.selectedCnp = '';
                }
                console.log(msg);
            });
        }


        $scope.$on('$destroy', unsubscribe);
    }
})();
