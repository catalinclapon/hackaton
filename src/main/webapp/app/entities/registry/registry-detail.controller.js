(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('RegistryDetailController', RegistryDetailController);

    RegistryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Registry', 'RegistryData', '$translate', '$interval'];

    function RegistryDetailController($scope, $rootScope, $stateParams, previousState, entity, Registry, RegistryData, $translate, $interval) {
        var vm = this, fields = entity.fields, fieldIds = [];

        vm.registry = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackatonApp:registryUpdate', function(event, result) {
            vm.registry = result;
        });

        var columnDefs = [{
            name: 'CNP', enableHiding: false
        }, {
            name: 'Name'
        }];


        if(angular.isDefined(entity.fields)){

            fields.sort(function(a,b) {return (a.category > b.category) ? 1 : ((b.category > a.category) ? -1 :
                ( (a.order > b.order) ? 1 : ((b.order > a.order) ? -1 : 0) ) );
            });
            fields.forEach(function(item, index) {
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
            onRegisterApi: function( gridApi ){
                $scope.gridApi = gridApi;

                // interval of zero just to allow the directive to have initialized
                //$interval( function() {
                //    gridApi.core.addToGridMenu( gridApi.grid, [{ title: 'Dynamic item', order: 100}]);
                //}, 0, 1);

                gridApi.core.on.columnVisibilityChanged( $scope, function( changedColumn ){
                    $scope.columnChanged = { name: changedColumn.colDef.name, visible: changedColumn.colDef.visible };
                });
            }
        };



        $scope.$on('$destroy', unsubscribe);
    }
})();
