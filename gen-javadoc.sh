#!/bin/sh


set -ex

jdk_source_path=/tmp/jdk-src 

unzip -q "${JAVA_HOME}/lib/src.zip" -d "$jdk_source_path"

cd "${jdk_source_path}" || exit 1
java_modules=$(for module in *; do echo --module "$module"; done)
javadoc -d /tmp/javadoc --module-source-path . $java_modules

rm -rf "$jdk_source_path"
