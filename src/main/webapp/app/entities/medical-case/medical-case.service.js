(function() {
    'use strict';
    angular
        .module('hackatonApp')
        .factory('MedicalCase', MedicalCase);

    MedicalCase.$inject = ['$resource'];

    function MedicalCase ($resource) {
        var resourceUrl =  'api/medical-cases/:registryId/:cnp';//:registryId/:cnp

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
