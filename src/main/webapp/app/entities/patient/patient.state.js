(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('patient', {
            parent: 'entity',
            url: '/patient?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT'],
                pageTitle: 'hackatonApp.patient.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/patient/patients.html',
                    controller: 'PatientController',
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
                    $translatePartialLoader.addPart('patient');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('patient-detail', {
            parent: 'patient',
            url: '/patient/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT'],
                pageTitle: 'hackatonApp.patient.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/patient/patient-detail.html',
                    controller: 'PatientDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('patient');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Patient', function($stateParams, Patient) {
                    return Patient.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'patient',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('patient-detail.edit', {
            parent: 'patient-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/patient/patient-dialog.html',
                    controller: 'PatientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Patient', function(Patient) {
                            return Patient.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('patient.new', {
            parent: 'patient',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/patient/patient-dialog.html',
                    controller: 'PatientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                cnp: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('patient', null, { reload: 'patient' });
                }, function() {
                    $state.go('patient');
                });
            }]
        })
        .state('patient.edit', {
            parent: 'patient',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/patient/patient-dialog.html',
                    controller: 'PatientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Patient', function(Patient) {
                            return Patient.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('patient', null, { reload: 'patient' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('patient.delete', {
            parent: 'patient',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_DOCTOR','ROLE_PROVIDER','ROLE_PATIENT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/patient/patient-delete-dialog.html',
                    controller: 'PatientDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Patient', function(Patient) {
                            return Patient.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('patient', null, { reload: 'patient' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
