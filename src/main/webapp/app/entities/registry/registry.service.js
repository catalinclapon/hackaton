(function() {
    'use strict';
    angular
        .module('hackatonApp')
        .factory('Registry', Registry);

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
            'update': { method:'PUT' }
        });
    }
})();
