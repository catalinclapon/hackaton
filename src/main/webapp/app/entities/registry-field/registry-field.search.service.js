(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .factory('RegistryFieldSearch', RegistryFieldSearch);

    RegistryFieldSearch.$inject = ['$resource'];

    function RegistryFieldSearch($resource) {
        var resourceUrl =  'api/_search/registry-fields/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
