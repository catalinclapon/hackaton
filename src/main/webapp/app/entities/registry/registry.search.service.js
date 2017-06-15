(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .factory('RegistrySearch', RegistrySearch);

    RegistrySearch.$inject = ['$resource'];

    function RegistrySearch($resource) {
        var resourceUrl =  'api/_search/registries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
