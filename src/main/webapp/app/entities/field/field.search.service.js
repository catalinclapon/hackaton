(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .factory('FieldSearch', FieldSearch);

    FieldSearch.$inject = ['$resource'];

    function FieldSearch($resource) {
        var resourceUrl =  'api/_search/fields/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
