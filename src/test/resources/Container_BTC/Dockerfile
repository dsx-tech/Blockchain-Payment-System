FROM ubuntu:16.04

RUN apt-get update
RUN apt-get install --yes software-properties-common
RUN add-apt-repository --yes ppa:bitcoin/bitcoin
RUN apt-get update
RUN apt-get install --yes bitcoind

COPY . /home/bitcoind-testnet

EXPOSE 18443
EXPOSE 18444

ENTRYPOINT ["/home/bitcoind-testnet/entrypoint.sh"]