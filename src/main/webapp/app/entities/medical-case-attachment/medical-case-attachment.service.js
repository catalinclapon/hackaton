(function() {
    'use strict';
    angular
        .module('hackatonApp')
        .factory('MedicalCaseAttachment', MedicalCaseAttachment);

    MedicalCaseAttachment.$inject = ['$resource'];

    function MedicalCaseAttachment ($resource) {
        var resourceUrl =  'api/medical-case-attachments/:id';

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
