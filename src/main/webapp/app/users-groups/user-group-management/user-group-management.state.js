(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-group-management', {
            parent: 'users-groups',
            url: '/user-group-management?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_PROVIDER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/users-groups/user-group-management/user-group-management.html',
                    controller: 'UserGroupController',
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
                    $translatePartialLoader.addPart('userGroup');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-group-management-detail', {
            parent: 'user-group-management',
            url: '/user-group-management/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_PROVIDER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/users-groups/user-group-management/user-group-management-detail.html',
                    controller: 'UserGroupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userGroup');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserGroup', function($stateParams, UserGroup) {
                    return UserGroup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-group-management',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-group-management-detail.edit', {
            parent: 'user-group-management-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_PROVIDER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/users-groups/user-group-management/user-group-management-dialog.html',
                    controller: 'UserGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserGroup', function(UserGroup) {
                            return UserGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-group-management.new', {
            parent: 'user-group-management',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_PROVIDER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/users-groups/user-group-management/user-group-management-dialog.html',
                    controller: 'UserGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-group-management', null, { reload: 'user-group-management' });
                }, function() {
                    $state.go('user-group-management');
                });
            }]
        })
        .state('user-group-management.edit', {
            parent: 'user-group-management',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_PROVIDER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/users-groups/user-group-management/user-group-management-dialog.html',
                    controller: 'UserGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserGroup', function(UserGroup) {
                            return UserGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-group-management', null, { reload: 'user-group-management' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-group-management.delete', {
            parent: 'user-group-management',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_PROVIDER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/users-groups/user-group-management/user-group-management-delete-dialog.html',
                    controller: 'UserGroupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserGroup', function(UserGroup) {
                            return UserGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-group-management', null, { reload: 'user-group-management' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
