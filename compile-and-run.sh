#!/usr/bin/env bash
set -euo pipefail
rm -rf out
mkdir -p out
javac --release 25 -encoding UTF-8 -d out $(find src/main/java -name '*.java')
java -Dfile.encoding=UTF-8 -cp out zoo.Demo
