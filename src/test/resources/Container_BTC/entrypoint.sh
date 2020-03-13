#!/bin/bash
	export alice_dir=/home/bitcoind-testnet/nodes/alice
	export bob_dir=/home/bitcoind-testnet/nodes/bob
	(bitcoind -datadir=$alice_dir -deprecatedrpc=generate &> /home/bitcoind-testnet/nodes/alice/alice.log  &)
	(bitcoind -datadir=$bob_dir -deprecatedrpc=generate &> /home/bitcoind-testnet/nodes/bob/bob.log  &)
	while ! ( ( grep -q 'init message: Done loading' /home/bitcoind-testnet/nodes/alice/alice.log ) && ( grep -q 'init message: Done loading' /home/bitcoind-testnet/nodes/bob/bob.log) )
	do
		sleep 4
		echo "Waiting for node..."
	done
	bitcoin-cli -regtest -datadir=$alice_dir generatetoaddress 101 $(bitcoin-cli -regtest -datadir=$alice_dir getnewaddress)
	bitcoin-cli -regtest -datadir=$bob_dir generatetoaddress 101 $(bitcoin-cli -regtest -datadir=$bob_dir getnewaddress)
	echo "The node is ready!"
	sleep infinity
