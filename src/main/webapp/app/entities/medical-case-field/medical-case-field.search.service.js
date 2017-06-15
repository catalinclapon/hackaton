(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .factory('MedicalCaseFieldSearch', MedicalCaseFieldSearch);

    MedicalCaseFieldSearch.$inject = ['$resource'];

    function MedicalCaseFieldSearch($resource) {
        var resourceUrl =  'api/_search/medical-case-fields/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
