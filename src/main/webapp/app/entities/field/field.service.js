(function() {
    'use strict';
    angular
        .module('hackatonApp')
        .factory('Field', Field);

    Field.$inject = ['$resource'];

    function Field ($resource) {
        var resourceUrl =  'api/fields/:id';

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
