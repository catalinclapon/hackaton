(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .factory('MedicalCaseSearch', MedicalCaseSearch);

    MedicalCaseSearch.$inject = ['$resource'];

    function MedicalCaseSearch($resource) {
        var resourceUrl =  'api/_search/medical-cases/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
