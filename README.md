[![Build Status](https://travis-ci.com/dsx-tech/Blockchain-Payment-System.svg?branch=master)](https://travis-ci.com/dsx-tech/Blockchain-Payment-System)

# Blockchain-Payment-System (BPS)
Blockchain Payment System is an open source payment system
 for working with various cryptocurrencies.

## Supported Cryptocurrencies
- Bitcoin (BTC)
- TON Gram (GRM)
- Ripple (XRP)
- TRON (TRX)

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
To run BPS unit tests, you must run command ``gradle test``

## Payments and Invoices
BPS operates with two
 entities - Payment and Invoice.

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
  
### Detailed description of the system
Coursework with a detailed description of the system
- [Coursework by Dmitry Pogrebnoy (ru)](http://se.math.spbu.ru/SE/YearlyProjects/vesna-2020/pi/Pogrebnoy-report.pdf)
- Coursework by Artyom Chemezov - awaiting publication
- Coursework by Artyom Lunev - awaiting publication
- [Coursework by Sergey Skaredov (ru)](http://se.math.spbu.ru/SE/YearlyProjects/vesna-2020/pi/Pogrebnoy-report.pdf)