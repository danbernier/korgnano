rm -rf classes
mkdir classes
javac -d classes -Xlint src/**/*.java

jar -cvf korgnano.jar -C classes/ .
