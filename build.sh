#!/bin/sh

set -ex

find src/main -type f -name '*.java' -print0 \
    | xargs -0 javac -Xlint -Werror -d target/

jar --create --file build-java.jar --main-class build.java.Cli -C target/ .


