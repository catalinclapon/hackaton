(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .factory('GroupsSearch', GroupsSearch);

    GroupsSearch.$inject = ['$resource'];

    function GroupsSearch($resource) {
        var resourceUrl =  'api/_search/groups/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
