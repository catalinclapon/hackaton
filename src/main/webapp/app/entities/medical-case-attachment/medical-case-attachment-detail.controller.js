(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('MedicalCaseAttachmentDetailController', MedicalCaseAttachmentDetailController);

    MedicalCaseAttachmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MedicalCaseAttachment', 'MedicalCase'];

    function MedicalCaseAttachmentDetailController($scope, $rootScope, $stateParams, previousState, entity, MedicalCaseAttachment, MedicalCase) {
        var vm = this;

        vm.medicalCaseAttachment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackatonApp:medicalCaseAttachmentUpdate', function(event, result) {
            vm.medicalCaseAttachment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
