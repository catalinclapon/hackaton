(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .factory('UserGroupSearch', UserGroupSearch);

    UserGroupSearch.$inject = ['$resource'];

    function UserGroupSearch($resource) {
        var resourceUrl =  'api/_search/user-groups/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
