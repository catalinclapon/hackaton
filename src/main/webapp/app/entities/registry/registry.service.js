(function() {
    'use strict';
    angular
        .module('hackatonApp')
        .factory('Registry', Registry)
        .factory('RegistryData', RegistryData);

    Registry.$inject = ['$resource'];

    function Registry ($resource) {
        var resourceUrl =  'api/registries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'getData': { method: 'POST', isArray: true },
            'update': { method:'PUT' }
        });
    }

    RegistryData.$inject = ['$resource'];

    function RegistryData ($resource) {
        var resourceUrl =  'api/registries/:id/data';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
