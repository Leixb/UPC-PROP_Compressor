#!/usr/bin/env bash

set -e

DRIVER=${1:-JPEG}

echo "Build & run ${DRIVER}Driver"

DRIVER_CP=target/driver-classes-$DRIVER
DRIVER_SR=target/driver-sources-$DRIVER

echo -n "Borrando builds anteriores..."
{ rm -r $DRIVER_SR $DRIVER_CP ; } || true
echo -e "\rBorrando builds anteriores DONE"

mkdir -p $DRIVER_CP $DRIVER_SR/domini

echo -n "Uniendo codigo de drivers y stubs..."
cp src/test/stubs/*.java $DRIVER_SR/domini
cp src/test/drivers/${DRIVER}Driver.java $DRIVER_SR
cp src/main/java/domini/${DRIVER}.java $DRIVER_SR/domini
cp src/test/resources/* ${DRIVER_CP}
echo -e "\rUniendo codigo de drivers y stubs DONE"

echo -n "Compilando classes..."
javac -d $DRIVER_CP $DRIVER_SR/*.java $DRIVER_SR/domini/*.java
echo -e "\rCompilando Classes DONE"

echo "Ejecutando ${DRIVER}Driver:"
java -cp $DRIVER_CP ${DRIVER}Driver
