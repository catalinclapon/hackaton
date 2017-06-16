(function () {
    'use strict';

    angular
        .module('hackatonApp')
        .factory('UploadBulk', UploadBulk);

    UploadBulk.$inject = ['$resource'];

    function UploadBulk ($resource) {
        var service = $resource('api/registries/saveBulk', {}, {});
        return service;
    }
})();
