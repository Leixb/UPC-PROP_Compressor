DRIVER=${1:-JPEG}

DRIVER_CP=target/driver-classes-$DRIVER
DRIVER_SR=target/driver-sources-$DRIVER

rm -r $DRIVER_SR $DRIVER_CP

mkdir -p $DRIVER_CP $DRIVER_SR/domini

cp src/test/stubs/*.java $DRIVER_SR/domini
cp src/test/drivers/${DRIVER}Driver.java $DRIVER_SR
cp src/main/java/domini/${DRIVER}.java $DRIVER_SR/domini

javac -d $DRIVER_CP $DRIVER_SR/*.java $DRIVER_SR/domini/*.java

java -cp $DRIVER_CP ${DRIVER}Driver
