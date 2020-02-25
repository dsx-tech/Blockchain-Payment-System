FROM ubuntu:xenial

RUN apt-get update \
     && apt-get install -y wget \
     && rm -rf /var/lib/apt/lists/* 

ARG BINARY="geth-linux-amd64-1.9.10-58cf5686.tar.gz"
RUN wget "https://gethstore.blob.core.windows.net/builds/$BINARY"
RUN tar -xzvf $BINARY --strip 1
RUN rm $BINARY

ADD ./genesis.json ./genesis.json

COPY datadir /datadir
COPY geth.password ./geth.password
CMD exec ./geth --minerthreads 1 --datadir="/datadir" --allow-insecure-unlock --rpcapi eth,web3,personal,admin  --password /geth.password --unlock "0" --port 30303 --rpc --rpcaddr "0.0.0.0" --networkid=239301 --identity="test-dev-03-01" --rpcport 8545 --verbosity 6 --maxpeers=4 --mine

EXPOSE 8545
EXPOSE 30303


