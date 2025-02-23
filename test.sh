#!/bin/sh


./build.sh || exit 1

cd ./examples/hello_world

java -jar ../../build-java.jar Build.java 
