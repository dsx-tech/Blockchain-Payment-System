[![Build Status](https://travis-ci.com/dsx-tech/Blockchain-Payment-System.svg?branch=master)](https://travis-ci.com/dsx-tech/Blockchain-Payment-System)

# Blockchain-Payment-System (BPS)
Blockchain Payment System is an open source payment system
 for working with various cryptocurrencies.

## Supported Cryptocurrencies
- Bitcoin (BTC)
- TON Gram (GRM)
- Ripple (XRP)
- TRON (TRX)
- Ethereum (ETH)
- Tether (USDT)


## Requirements

- Kotlin 1.3.70
- Gradle 4.10

#### TON GRAM
To work with the TON Gram cryptocurrency,
 you must run a payment system on Linux 18.04
  or build a ton-nativelib for a specific OS.
  
A docker image for build the ton-nativelib on Linux 18.04 is located at
```./src/main/resources/DockerImages.ton-nativelib-image```
## How to run
#### Сonfiguring
Before starting a BPS, it is necessary
 to set cryptocurrencies and its settings in the 
 configuration file. 
 
 BPS configuration file is located at 
 ``./src/main/resources/BpsConfig.yaml``
 
 A detailed description of the parameters is in the comments of the configuration file.

#### Starting
The system is started using the Gradle command.
1. First you need to build a BPS 
```gradle build```
2. After that run it
```gradle run```

#### Testing
Docker is required to run unit tests.
To run BPS unit tests, you must run command ``gradle test``

###### Expand the network manually

- BTC:
You can use a container https://hub.docker.com/repository/docker/siandreev/bitcoind-regtest

To do this, run ``docker run -p 18443:18443 -p 18444:18444 siandreev/bitcoind-regtest:alice-bob-regtest``

You can interact with the network through the JSON-RPC using curl

Find out the balance of Alice: ``curl --user alice:password --data-binary '{"jsonrpc": "1.0", "id":"curltest", "method": "get
               balance", "params": [] }'  -H 'content-type: text/plain;' http://127.0.0.1:18443/``
               
Send 10 BTC to Bob: ``curl --user alice:password --data-binary '{"jsonrpc": "1.0", "id":"curltest", "method": "sendtoaddress", "params": ["2NETNm86ug9drkCJ7N4U5crA9B9681HidzX", 10] }'  -H 'content-type: text/plain;' http://127.0.0.1:18443/``

- ETH: You can use a container https://hub.docker.com/repository/docker/siandreev/ethereum-rpc-test with "PoA-mining" tag

To do this, run ``docker run -p 8541:8541 -p 8542:8542 siandreev/ethereum-rpc-test:PoA-mining``

You can interact with the network using geth: run ``geth attach http://localhost:8541`` to connect to the node

Find out the balance of Alice: ``eth.getBalance("0x073cfa4b6635b1a1b96f6363a9e499a8076b6107")``

Send 10 ETH to Bob: ``eth.sendTransaction({from: "0x073cfa4b6635b1a1b96f6363a9e499a8076b6107",to: "0x0ce59225bcd447feaed698ed754d309feba5fc63",value: web3.toWei(10, "ether")});``

## Payments and Invoices
BPS operates with three
 entities - Payment, Invoice and DepositAccounts.

Payment - an entity is responsible for the withdrawal of cryptocurrency funds from the BPS account. 

Invoice - an entity is responsible for depositing cryptocurrency funds to the BPS account.
### BPS REST API
- ```GET http://localhost:8080/balance/{currency}``` - get account balance for a given cryptocurrency
- ```GET http://localhost:8080/invoice/{id}``` - get invoice for a given invoice id
- ```GET http://localhost:8080/payment/{id}``` - get payment for a given payment id
- ```POST http://localhost:8080/invoice``` - create new invoice with a given parametrs
    - Example: 
    ```
    POST http://localhost:8080/invoice
    Content-Type: application/json
   
    {
      "currency": "GRM",
      "amount": "1"
    }
    ```
- ```POST http://localhost:8080/payment``` - create new payment with a given parametrs
    - Example:
    ```
    POST http://localhost:8080/payment
    Content-Type: application/json
    
    {
      "currency": "GRM",
      "amount": "1",
      "address": "kQCBFvmeIOjrZlSoxIVys2nE7nQUxUPM370tbmyC_lzlaIlk",
      "tag": "TEST"
    }
    ```

## Deposit accounts
Deposit account - an entity, that has multiple addresses, to which funds are constantly received.
### BPS API
- ```createNewAccount``` - receives account id, list of currencies you want to use and creates account
- ```createNewAddress``` - receives account id, address currency and return created address
- ```getDepositAccount``` - receives account id and return deposit account
- ```getAllTx``` - receives account id, currency and return all transactions to account addresses
- ```getLastTxToAddress``` - receives account id, currency, address, amount of transactions and return last transactions to address

### Detailed description of the system
Coursework with a detailed description of the system
- [Coursework by Dmitry Pogrebnoy (ru)](http://se.math.spbu.ru/SE/YearlyProjects/vesna-2020/pi/Pogrebnoy-report.pdf)
- Coursework by Artyom Chemezov - awaiting publication
- [Coursework by Sergey Skaredov (ru)](http://se.math.spbu.ru/SE/YearlyProjects/spring-2019/371/Skaredov-report.pdf)