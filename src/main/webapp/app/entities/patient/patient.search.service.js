(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .factory('PatientSearch', PatientSearch);

    PatientSearch.$inject = ['$resource'];

    function PatientSearch($resource) {
        var resourceUrl =  'api/_search/patients/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
