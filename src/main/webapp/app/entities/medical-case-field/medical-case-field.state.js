(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('medical-case-field', {
            parent: 'entity',
            url: '/medical-case-field?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT'],
                pageTitle: 'hackatonApp.medicalCaseField.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medical-case-field/medical-case-fields.html',
                    controller: 'MedicalCaseFieldController',
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
                    $translatePartialLoader.addPart('medicalCaseField');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('medical-case-field-detail', {
            parent: 'medical-case-field',
            url: '/medical-case-field/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT'],
                pageTitle: 'hackatonApp.medicalCaseField.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medical-case-field/medical-case-field-detail.html',
                    controller: 'MedicalCaseFieldDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('medicalCaseField');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MedicalCaseField', function($stateParams, MedicalCaseField) {
                    return MedicalCaseField.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'medical-case-field',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('medical-case-field-detail.edit', {
            parent: 'medical-case-field-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case-field/medical-case-field-dialog.html',
                    controller: 'MedicalCaseFieldDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MedicalCaseField', function(MedicalCaseField) {
                            return MedicalCaseField.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medical-case-field.new', {
            parent: 'medical-case-field',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case-field/medical-case-field-dialog.html',
                    controller: 'MedicalCaseFieldDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                value: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('medical-case-field', null, { reload: 'medical-case-field' });
                }, function() {
                    $state.go('medical-case-field');
                });
            }]
        })
        .state('medical-case-field.edit', {
            parent: 'medical-case-field',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case-field/medical-case-field-dialog.html',
                    controller: 'MedicalCaseFieldDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MedicalCaseField', function(MedicalCaseField) {
                            return MedicalCaseField.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medical-case-field', null, { reload: 'medical-case-field' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medical-case-field.delete', {
            parent: 'medical-case-field',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case-field/medical-case-field-delete-dialog.html',
                    controller: 'MedicalCaseFieldDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MedicalCaseField', function(MedicalCaseField) {
                            return MedicalCaseField.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medical-case-field', null, { reload: 'medical-case-field' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
