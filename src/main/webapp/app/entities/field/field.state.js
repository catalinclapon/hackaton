(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('field', {
            parent: 'entity',
            url: '/field?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT'],
                pageTitle: 'hackatonApp.field.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/field/fields.html',
                    controller: 'FieldController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('field');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('field-detail', {
            parent: 'field',
            url: '/field/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT'],
                pageTitle: 'hackatonApp.field.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/field/field-detail.html',
                    controller: 'FieldDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('field');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Field', function($stateParams, Field) {
                    return Field.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'field',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('field-detail.edit', {
            parent: 'field-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/field/field-dialog.html',
                    controller: 'FieldDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Field', function(Field) {
                            return Field.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('field.new', {
            parent: 'field',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/field/field-dialog.html',
                    controller: 'FieldDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                desc: null,
                                type: null,
                                min: null,
                                required: null,
                                max: null,
                                extValidation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('field', null, { reload: 'field' });
                }, function() {
                    $state.go('field');
                });
            }]
        })
        .state('field.edit', {
            parent: 'field',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/field/field-dialog.html',
                    controller: 'FieldDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Field', function(Field) {
                            return Field.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('field', null, { reload: 'field' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('field.delete', {
            parent: 'field',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/field/field-delete-dialog.html',
                    controller: 'FieldDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Field', function(Field) {
                            return Field.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('field', null, { reload: 'field' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
