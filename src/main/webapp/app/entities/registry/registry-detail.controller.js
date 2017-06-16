(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('RegistryDetailController', RegistryDetailController);

    RegistryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Registry', 'RegistryData', '$interval'];

    function RegistryDetailController($scope, $rootScope, $stateParams, previousState, entity, Registry, RegistryData, $interval) {
        var vm = this, fields = entity.fields, fieldIds = [];

        vm.registry = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackatonApp:registryUpdate', function(event, result) {
            vm.registry = result;
        });

        var fakeI18n = function( title ){
            var deferred = $q.defer();
            $interval( function() {
                deferred.resolve( 'col: ' + title );
            }, 1000, 1);
            return deferred.promise;
        };

        var columnDefs = [{
            name: 'CNP', enableHiding: false
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
            gridMenuTitleFilter: fakeI18n,
            columnDefs: columnDefs,
            gridMenuCustomItems: [
                {
                    title: entity.name,
                    action: function ($event) {
                        this.grid.element.toggleClass('rotated');
                    },
                    order: 210
                }
            ],
            data: RegistryData.query({id: entity.id, fields: fieldIds}),
            onRegisterApi: function( gridApi ){
                $scope.gridApi = gridApi;

                // interval of zero just to allow the directive to have initialized
                $interval( function() {
                    gridApi.core.addToGridMenu( gridApi.grid, [{ title: 'Dynamic item', order: 100}]);
                }, 0, 1);

                gridApi.core.on.columnVisibilityChanged( $scope, function( changedColumn ){
                    $scope.columnChanged = { name: changedColumn.colDef.name, visible: changedColumn.colDef.visible };
                });
            }
        };



        $scope.$on('$destroy', unsubscribe);
    }
})();
