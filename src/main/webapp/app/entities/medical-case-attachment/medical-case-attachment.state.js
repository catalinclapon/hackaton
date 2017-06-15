(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('medical-case-attachment', {
            parent: 'entity',
            url: '/medical-case-attachment?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackatonApp.medicalCaseAttachment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medical-case-attachment/medical-case-attachments.html',
                    controller: 'MedicalCaseAttachmentController',
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
                    $translatePartialLoader.addPart('medicalCaseAttachment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('medical-case-attachment-detail', {
            parent: 'medical-case-attachment',
            url: '/medical-case-attachment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackatonApp.medicalCaseAttachment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medical-case-attachment/medical-case-attachment-detail.html',
                    controller: 'MedicalCaseAttachmentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('medicalCaseAttachment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MedicalCaseAttachment', function($stateParams, MedicalCaseAttachment) {
                    return MedicalCaseAttachment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'medical-case-attachment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('medical-case-attachment-detail.edit', {
            parent: 'medical-case-attachment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case-attachment/medical-case-attachment-dialog.html',
                    controller: 'MedicalCaseAttachmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MedicalCaseAttachment', function(MedicalCaseAttachment) {
                            return MedicalCaseAttachment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medical-case-attachment.new', {
            parent: 'medical-case-attachment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case-attachment/medical-case-attachment-dialog.html',
                    controller: 'MedicalCaseAttachmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                location: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('medical-case-attachment', null, { reload: 'medical-case-attachment' });
                }, function() {
                    $state.go('medical-case-attachment');
                });
            }]
        })
        .state('medical-case-attachment.edit', {
            parent: 'medical-case-attachment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case-attachment/medical-case-attachment-dialog.html',
                    controller: 'MedicalCaseAttachmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MedicalCaseAttachment', function(MedicalCaseAttachment) {
                            return MedicalCaseAttachment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medical-case-attachment', null, { reload: 'medical-case-attachment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medical-case-attachment.delete', {
            parent: 'medical-case-attachment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case-attachment/medical-case-attachment-delete-dialog.html',
                    controller: 'MedicalCaseAttachmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MedicalCaseAttachment', function(MedicalCaseAttachment) {
                            return MedicalCaseAttachment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medical-case-attachment', null, { reload: 'medical-case-attachment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
