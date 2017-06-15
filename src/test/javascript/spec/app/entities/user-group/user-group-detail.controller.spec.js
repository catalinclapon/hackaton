'use strict';

describe('Controller Tests', function() {

    describe('UserGroup Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUserGroup, MockUser, MockGroups;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUserGroup = jasmine.createSpy('MockUserGroup');
            MockUser = jasmine.createSpy('MockUser');
            MockGroups = jasmine.createSpy('MockGroups');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UserGroup': MockUserGroup,
                'User': MockUser,
                'Groups': MockGroups
            };
            createController = function() {
                $injector.get('$controller')("UserGroupDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hackatonApp:userGroupUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
