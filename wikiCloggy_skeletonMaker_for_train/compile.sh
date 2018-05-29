#!/bin/sh
rm -rf bin
mkdir bin
javac -d bin -sourcepath src -cp src src/Wikicloggy/Wikicloggy.java
