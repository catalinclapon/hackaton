(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('RegistryFieldDetailController', RegistryFieldDetailController);

    RegistryFieldDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RegistryField', 'Registry', 'Field'];

    function RegistryFieldDetailController($scope, $rootScope, $stateParams, previousState, entity, RegistryField, Registry, Field) {
        var vm = this;

        vm.registryField = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackatonApp:registryFieldUpdate', function(event, result) {
            vm.registryField = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
