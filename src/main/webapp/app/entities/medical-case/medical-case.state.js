(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('medical-case', {
            parent: 'entity',
            url: '/medical-case?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT'],
                pageTitle: 'hackatonApp.medicalCase.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medical-case/medical-cases.html',
                    controller: 'MedicalCaseController',
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
                    $translatePartialLoader.addPart('medicalCase');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('medical-case-detail', {
            parent: 'medical-case',
            url: '/medical-case/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT'],
                pageTitle: 'hackatonApp.medicalCase.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medical-case/medical-case-detail.html',
                    controller: 'MedicalCaseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('medicalCase');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MedicalCase', function($stateParams, MedicalCase) {
                    return MedicalCase.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'medical-case',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('medical-case-detail.edit', {
            parent: 'medical-case-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case/medical-case-dialog.html',
                    controller: 'MedicalCaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MedicalCase', function(MedicalCase) {
                            return MedicalCase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medical-case.new', {
            parent: 'medical-case',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case/medical-case-dialog.html',
                    controller: 'MedicalCaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                uuid: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('medical-case', null, { reload: 'medical-case' });
                }, function() {
                    $state.go('medical-case');
                });
            }]
        })
        .state('medical-case.edit', {
            parent: 'medical-case',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case/medical-case-dialog.html',
                    controller: 'MedicalCaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MedicalCase', function(MedicalCase) {
                            return MedicalCase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medical-case', null, { reload: 'medical-case' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medical-case.delete', {
            parent: 'medical-case',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case/medical-case-delete-dialog.html',
                    controller: 'MedicalCaseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MedicalCase', function(MedicalCase) {
                            return MedicalCase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medical-case', null, { reload: 'medical-case' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
