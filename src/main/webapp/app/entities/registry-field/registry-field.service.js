(function() {
    'use strict';
    angular
        .module('hackatonApp')
        .factory('RegistryField', RegistryField);

    RegistryField.$inject = ['$resource'];

    function RegistryField ($resource) {
        var resourceUrl =  'api/registry-fields/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
