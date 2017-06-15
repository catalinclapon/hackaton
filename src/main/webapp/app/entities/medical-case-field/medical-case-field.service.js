(function() {
    'use strict';
    angular
        .module('hackatonApp')
        .factory('MedicalCaseField', MedicalCaseField);

    MedicalCaseField.$inject = ['$resource'];

    function MedicalCaseField ($resource) {
        var resourceUrl =  'api/medical-case-fields/:id';

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
