'use strict';

describe('Controller Tests', function() {

    describe('RegistryField Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRegistryField, MockRegistry, MockField;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRegistryField = jasmine.createSpy('MockRegistryField');
            MockRegistry = jasmine.createSpy('MockRegistry');
            MockField = jasmine.createSpy('MockField');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'RegistryField': MockRegistryField,
                'Registry': MockRegistry,
                'Field': MockField
            };
            createController = function() {
                $injector.get('$controller')("RegistryFieldDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hackatonApp:registryFieldUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
