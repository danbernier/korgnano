./build.sh

rm -rf test_classes
mkdir test_classes

javac -d test_classes \
  -classpath lib/junit/junit-4.12.jar:lib/junit/hamcrest-core-1.3.jar:classes \
  -Xlint test/**/*.java

java \
  -classpath lib/junit/junit-4.12.jar:lib/junit/hamcrest-core-1.3.jar:classes:test_classes \
  org.junit.runner.JUnitCore \
  $(ls test/**/*.java | sed 's/^test\///g' | sed 's/\.java//g' | sed 's/\//./g')
