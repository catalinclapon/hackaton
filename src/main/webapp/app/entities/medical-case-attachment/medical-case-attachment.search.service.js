(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .factory('MedicalCaseAttachmentSearch', MedicalCaseAttachmentSearch);

    MedicalCaseAttachmentSearch.$inject = ['$resource'];

    function MedicalCaseAttachmentSearch($resource) {
        var resourceUrl =  'api/_search/medical-case-attachments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
