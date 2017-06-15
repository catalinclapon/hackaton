(function () {
    'use strict';

    angular
        .module('hackatonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig ($stateProvider) {
        $stateProvider.state('users-groups', {
            abstract: true,
            parent: 'app'
        });
    }
})();
