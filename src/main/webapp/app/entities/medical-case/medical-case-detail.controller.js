(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('MedicalCaseDetailController', MedicalCaseDetailController);

    MedicalCaseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MedicalCase', 'Patient'];

    function MedicalCaseDetailController($scope, $rootScope, $stateParams, previousState, entity, MedicalCase, Patient) {
        var vm = this;

        vm.medicalCase = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackatonApp:medicalCaseUpdate', function(event, result) {
            vm.medicalCase = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
