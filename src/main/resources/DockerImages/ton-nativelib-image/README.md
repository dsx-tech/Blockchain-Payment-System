# Ton nativelib image
This image create to compile the TON nativelib library for Ubuntu 18.04.

[Check on Docker hub](https://hub.docker.com/r/pogrebnoy/ton-native-lib)
#### How to build and run
To build the image, run the command:
```
docker build -t ton-native-lib:1.0 <path to Dockerfile folder>
```
To run the container, run the command:
```
docker run -it --mount type=bind,source=<path to java class folder>,target=/home/javaClasses --mount type=bind,source=<path to jniLibs folder>,target=/home/jniLibs ton-native-lib:1.0
```
#### How it works
- When the container starts, two directories are mounted, where at the end of the container’s work there will be a compiled TON nativelib library and related Java classes. 
- The compilation process can take a long time. It is recommended that you close other applications on your computer to increase the build performance.
- After the container finishes work, there will be TonApi.java and Client.java in the `<path to java class folder>` folder, the compiled library will be in the `<path to jniLibs folder>` folder.

#### How it use

The resulting Java classes and the TON nativelib library binary can be used to develop your applications by JNI.

For example check this project: [Blockchain Payment System](https://github.com/dsx-tech/Blockchain-Payment-System)
