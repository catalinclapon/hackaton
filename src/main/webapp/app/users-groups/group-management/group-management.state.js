(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('group-management', {
            parent: 'users-groups',
            url: '/group-management?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_PROVIDER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/users-groups/group-management/group-management.html',
                    controller: 'GroupsController',
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
                    $translatePartialLoader.addPart('groups');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('group-management-detail', {
            parent: 'group-management',
            url: '/group-management/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_PROVIDER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/users-groups/group-management/group-management-detail.html',
                    controller: 'GroupsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('groups');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Groups', function($stateParams, Groups) {
                    return Groups.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'groups',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('group-management-detail.edit', {
            parent: 'group-management-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_PROVIDER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/users-groups/group-management/group-management-dialog.html',
                    controller: 'GroupsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Groups', function(Groups) {
                            return Groups.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('group-management.new', {
            parent: 'group-management',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_PROVIDER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/users-groups/group-management/group-management-dialog.html',
                    controller: 'GroupsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('group-management', null, { reload: 'group-management' });
                }, function() {
                    $state.go('group-management');
                });
            }]
        })
        .state('group-management.edit', {
            parent: 'group-management',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_PROVIDER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/users-groups/group-management/group-management-dialog.html',
                    controller: 'GroupsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Groups', function(Groups) {
                            return Groups.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('group-management', null, { reload: 'group-management' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('group-management.delete', {
            parent: 'group-management',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_PROVIDER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/users-groups/group-management/group-management-delete-dialog.html',
                    controller: 'GroupsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Groups', function(Groups) {
                            return Groups.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('group-management', null, { reload: 'group-management' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
