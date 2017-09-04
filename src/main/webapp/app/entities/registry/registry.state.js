(function () {
    'use strict';

    angular
        .module('hackatonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('registry', {
                parent: 'entity',
                url: '/registry?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT'],
                    pageTitle: 'hackatonApp.registry.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/registry/registries.html',
                        controller: 'RegistryController',
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
                        $translatePartialLoader.addPart('registry');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('registry-detail', {
                parent: 'registry',
                url: '/registry/{id}',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT'],
                    pageTitle: 'hackatonApp.registry.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/registry/registry-detail.html',
                        controller: 'RegistryDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('registry');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Registry', function ($stateParams, Registry) {
                        return Registry.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'registry',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('registry-detail.edit', {
                parent: 'registry-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/registry/registry-dialog.html',
                        controller: 'RegistryDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Registry', function (Registry) {
                                return Registry.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('registry.new', {
                parent: 'registry',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/registry/registry-dialog.html',
                        controller: 'RegistryDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    desc: null,
                                    uuid: null,
                                    status: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('registry', null, {reload: 'registry'});
                    }, function () {
                        $state.go('registry');
                    });
                }]
            })
            .state('registry.edit', {
                parent: 'registry',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/registry/registry-dialog.html',
                        controller: 'RegistryDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Registry', function (Registry) {
                                return Registry.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('registry', null, {reload: 'registry'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('registry-detail.medical-case-new', {
                parent: 'registry-detail',
                url: '/medical-case-new',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PATIENT']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/medical-case/medical-case-dialog.html',
                        controller: 'MedicalCaseDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            registry: ['Registry', function (Registry) {
                                return Registry.get({id: $stateParams.id}).$promise;
                            }],
                            entity: function () {
                                return {
                                    name: null,
                                    uuid: null,
                                    status: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('registry-detail', {id: $stateParams.id}, {reload: 'registry-detail'});
                    }, function () {
                        $state.go('registry-detail');
                    });
                }]
            })
            .state('registry-detail.medical-case-edit', {
                parent: 'registry-detail',
                url: '/medical-case/{cnp}/edit',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
                },
                /*onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/medical-case/medical-case-dialog.html',
                        controller: 'MedicalCaseDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['MedicalCase', function(MedicalCase) {
                                //return MedicalCase.get({cnp : $stateParams.cnp}).$promise;
                                return {
                                    name: null,
                                    uuid: null,
                                    status: null,
                                    id: null
                                };
                            }]
                        }
                    }).result.then(function() {
                        $state.go('registry-detail', {id: $stateParams.id}, {reload: 'registry-detail'});
                    }, function() {
                        $state.go('registry-detail');
                    });
                }]*/
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/medical-case/medical-case-dialog.html',
                        controller: 'MedicalCaseDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            registry: ['Registry', function (Registry) {
                                return Registry.get({id: $stateParams.id}).$promise;
                            }],
                            entity: ['MedicalCase', function(MedicalCase) {
                                return MedicalCase.get({registryId: $stateParams.id, cnp : $stateParams.cnp}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('registry-detail', {id: $stateParams.id}, {reload: 'registry-detail'});
                    }, function () {
                        $state.go('registry-detail');
                    });
                }]
            })
            .state('registry.delete', {
                parent: 'registry',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/registry/registry-delete-dialog.html',
                        controller: 'RegistryDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Registry', function (Registry) {
                                return Registry.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('registry', null, {reload: 'registry'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('registry-detail.bulk-upload', {
                parent: 'registry-detail',
                url: '/detail/bulk-upload',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/registry/bulk-upload-dialog.html',
                        controller: 'BulkUploadController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Registry', function (Registry) {
                                return Registry.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    })
                        .result.then(function () {
                        $state.go('registry-detail', null, {reload: 'registry-detail'});
                    }, function () {
                        $state.go('registry-detail');
                    });
                }]
            });
    }

})();
