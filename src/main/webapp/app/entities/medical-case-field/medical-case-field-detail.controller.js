(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('MedicalCaseFieldDetailController', MedicalCaseFieldDetailController);

    MedicalCaseFieldDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MedicalCaseField', 'Field', 'MedicalCase'];

    function MedicalCaseFieldDetailController($scope, $rootScope, $stateParams, previousState, entity, MedicalCaseField, Field, MedicalCase) {
        var vm = this;

        vm.medicalCaseField = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackatonApp:medicalCaseFieldUpdate', function(event, result) {
            vm.medicalCaseField = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
