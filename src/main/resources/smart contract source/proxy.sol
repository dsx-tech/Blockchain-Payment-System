pragma solidity ^0.6.4;

contract Proxy{
    
    address payable owner;
    
    constructor() public {
        owner = msg.sender;
    }

     fallback() payable external {
        uint value = msg.value;
        owner.transfer(value);
    }
}