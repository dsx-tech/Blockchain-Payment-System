FROM ubuntu:xenial

RUN apt-get update && apt-get install --yes software-properties-common
RUN add-apt-repository ppa:ethereum/ethereum
RUN apt-get update && apt-get install --yes geth
RUN apt-get update && apt-get install --yes bootnode

COPY node1 /node1
COPY node2 /node2
COPY devnet.json ./devnet.json
COPY boot.key ./boot.key
COPY entrypoint.sh ./entrypoint.sh

EXPOSE 8541
EXPOSE 8542
EXPOSE 30310
EXPOSE 30311
EXPOSE 30312

ENTRYPOINT ["./entrypoint.sh"]


