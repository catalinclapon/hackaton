'use strict';

describe('Controller Tests', function() {

    describe('MedicalCaseAttachment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMedicalCaseAttachment, MockMedicalCase;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMedicalCaseAttachment = jasmine.createSpy('MockMedicalCaseAttachment');
            MockMedicalCase = jasmine.createSpy('MockMedicalCase');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MedicalCaseAttachment': MockMedicalCaseAttachment,
                'MedicalCase': MockMedicalCase
            };
            createController = function() {
                $injector.get('$controller')("MedicalCaseAttachmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hackatonApp:medicalCaseAttachmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
