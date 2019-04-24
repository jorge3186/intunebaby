[![Build Status](https://travis-ci.org/sqshq/PiggyMetrics.svg?branch=master)](https://travis-ci.org/sqshq/PiggyMetrics)
[![codecov.io](https://codecov.io/github/jorge3186/intunebaby/coverage.svg?branch=master)](https://codecov.io/github/jorge3186/intunebaby?branch=master)
[![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/jorge3186/intunebaby/blob/master/LICENCE)

# InTune Baby

**An application that takes the stress out of tracking those important things that come with a new baby**

### Prerequisites

This application stores it's secrets in a Hashicorp Vault instance that needs to be setup prior to running this application. The Vault is intentionally not included within this repo. The easiest way to do this is to use [vault's cli](https://www.vaultproject.io/docs/commands/). 
A basic example:
```bash
vault kv put secret/itb-generic itb.config-username=$USERNAME itb.config-password=$PASSWORD
vault kv get secret/itb-generic
```
The following keys (and their value) need to to be added to the vault with a backend of **/secret/itb-generic**:
- itb.config-username
- itb.config-password
- itb.signing-key (RSA Private Key)
- itb.verifier-key (RSA Public Key)
- itb.baby-password
- itb.diapers-password
- itb.feedings-password
- itb.sleep-password
- itb.notify-password

The vault is also utilized to provide database credentials for each service. Check [here](https://www.vaultproject.io/docs/secrets/databases/index.html) for more information on how to setup the database secret engine in vault. MongoDB is used for all databases and intergrated with spring-cloud-vault-config-databases. The following roles need to be generated in the vault:
- users-readwrite
- baby-readwrite
- diapers-readwrite
- feedings-readwrite
- sleep-readwrite
- notify-readwrite


After the vault has been updated, a **config.env** file must be placed at the root of this repository before running a make command. For help in building this, run the following:
```bash
make help
```