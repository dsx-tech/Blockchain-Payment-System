FROM ubuntu:bionic

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update && apt-get install openjdk-8-jdk git ninja-build g++ zlib1g-dev libssl-dev libreadline-dev php wget make -y && cd /tmp && wget https://cmake.org/files/v3.17/cmake-3.17.0.tar.gz && tar -xzvf cmake-3.17.0.tar.gz && cd /tmp/cmake-3.17.0/ && /tmp/cmake-3.17.0/bootstrap && make -j4 && make install && apt-get remove --purge make wget -y && rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
                       
COPY entrypoint.sh home/datadir/entrypoint.sh

ENTRYPOINT ["sh", "home/datadir/entrypoint.sh"]
