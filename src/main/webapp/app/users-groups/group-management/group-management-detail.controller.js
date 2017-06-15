(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('GroupsDetailController', GroupsDetailController);

    GroupsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Groups'];

    function GroupsDetailController($scope, $rootScope, $stateParams, previousState, entity, Groups) {
        var vm = this;

        vm.groups = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackatonApp:groupsUpdate', function(event, result) {
            vm.groups = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
