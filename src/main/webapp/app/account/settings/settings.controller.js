(function() {
    'use strict';

    angular
        .module('hackatonApp')
        .controller('SettingsController', SettingsController);

    SettingsController.$inject = ['Principal', 'Auth', 'JhiLanguageService', '$translate', 'UserPatient'];

    function SettingsController (Principal, Auth, JhiLanguageService, $translate, UserPatient) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.patientInfo = null;
        vm.settingsAccount = null;
        vm.success = null;

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login,
                isPatient: account.isPatient,
                cnp: account.cnp

            };
        };

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
            vm.patientInfo = UserPatient.get();
        });

        function save () {
            vm.settingsAccount.cnp = vm.patientInfo.cnp;
            vm.settingsAccount.isPatient = vm.patientInfo.patientFlag;

            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';

                Principal.identity(true).then(function(account) {
                    //vm.settingsAccount = copyAccount(account);
                    vm.settingsAccount.activated = account.activated;
                    vm.settingsAccount.email = account.email;
                    vm.settingsAccount.firstName = account.firstName;
                    vm.settingsAccount.langKey = account.langKey;
                    vm.settingsAccount.lastName = account.lastName;
                    vm.settingsAccount.activated = account.activated;
                    vm.settingsAccount.login = account.login;

                });
                JhiLanguageService.getCurrent().then(function(current) {
                    if (vm.settingsAccount.langKey !== current) {
                        $translate.use(vm.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }
    }
})();
