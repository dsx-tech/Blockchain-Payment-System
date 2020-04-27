!#bin/sh
# clone TON repository
cd /home
git clone --recursive https://github.com/ton-blockchain/ton.git
# hack cmake script
cd /home/ton/tl
sed -i 's/target_include_directories(tl_tonlib_api PUBLIC/target_include_directories(tl_tonlib_api PUBLIC\n  $<BUILD_INTERFACE:${JAVA_INCLUDE_PATH}>\n  $<BUILD_INTERFACE:${JAVA_INCLUDE_PATH2}>/' CMakeLists.txt
# build
mkdir /home/ton/example/android/build
cd /home/ton/example/android/build
cmake .. -DCMAKE_BUILD_TYPE=Release -G "Ninja" -DOPENSSL_ROOT_DIR=/usr/include/openssl -DTON_ONLY_TONLIB=ON -DTD_ENABLE_JNI=ON -DJAVA_INCLUDE_PATH=$JAVA_HOME/include -DJAVA_INCLUDE_PATH2=$JAVA_HOME/include/linux
cmake --build . --target native-lib
cmake --build . --target tl_generate_java

cd /home/ton/example/android
php AddIntDef.php src/drinkless/org/ton/TonApi.java
# copy result to mount dirs
mkdir -p /home/javaClasses/drinkless/org/ton
cp -vf /home/ton/example/android/src/drinkless/org/ton/TonApi.java /home/javaClasses/drinkless/org/ton
cp -vf /home/ton/example/android/src/drinkless/org/ton/Client.java /home/javaClasses/drinkless/org/ton
cp -vf /home/ton/example/android/build/libnative-lib.so /home/jniLibs
cp -vf /home/ton/example/android/build/libnative-lib.so.debug /home/jniLibs
printf "Successful!\nClient.java and TonApi.java are in the /home/javaClasses folder.\nlibnative-lib.so and libnative-lib.so.debug are in the /home/jniLibs folder.\n"

