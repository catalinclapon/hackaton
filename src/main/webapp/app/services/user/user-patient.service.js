(function () {
    'use strict';

    angular
        .module('hackatonApp')
        .factory('UserPatient', UserPatient);

    UserPatient.$inject = ['$resource'];

    function UserPatient($resource) {
        var service = $resource('api/userpatient/:login', {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });

        return service;
    }
})();
