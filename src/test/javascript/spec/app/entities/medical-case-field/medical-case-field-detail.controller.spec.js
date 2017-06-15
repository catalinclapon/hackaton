'use strict';

describe('Controller Tests', function() {

    describe('MedicalCaseField Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMedicalCaseField, MockField, MockMedicalCase;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMedicalCaseField = jasmine.createSpy('MockMedicalCaseField');
            MockField = jasmine.createSpy('MockField');
            MockMedicalCase = jasmine.createSpy('MockMedicalCase');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MedicalCaseField': MockMedicalCaseField,
                'Field': MockField,
                'MedicalCase': MockMedicalCase
            };
            createController = function() {
                $injector.get('$controller')("MedicalCaseFieldDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hackatonApp:medicalCaseFieldUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
