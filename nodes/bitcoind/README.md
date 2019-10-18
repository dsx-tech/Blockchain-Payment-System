## Setup local bitcoin net
1. Download and install [bitcoind](https://bitcoin.org/en/download) on your windows, linux or mac os
2. Add `<path/to/bitcoin>/bin` to your `PATH`
3. Set some env variables and aliases:
  ```
  # for windows
  set alice_dir=<path/to/project>/nodes/bitcoind/alice
  set bob_dir=<path/to/project>/nodes/bitcoind/bob
  doskey alice_btc=bitcoin-cli -datadir=%alice_dir% $*
  doskey bob_btc=bitcoin-cli -datadir=%bob_dir% $*
  
  # for linux and mac os
  export alice_dir=<path/to/project>/nodes/bitcoind/alice
  export bob_dir=<path/to/project>/nodes/bitcoind/bob
  alias alice_btc="bitcoin-cli -datadir=$alice_dir"
  alias bob_btc="bitcoin-cli -datadir=$bob_dir"  
  ```
4. Run nodes (use %alice_dir% on windows instead)
  ```
  $ bitcoind -datadir=$alice_dir -deprecatedrpc=generate # workaround for now
  $ bitcoind -datadir=$bob_dir -deprecatedrpc=generate
  ```
5. Check
  ```
  $ alice_btc getbalance
  $ bob_btc getbalance
  $ alice_btc generate 101
  $ bob_btc generate 101
  $ alice_btc getbalance
  $ bob_btc getbalance
  ```
6. Try to run some tests from project