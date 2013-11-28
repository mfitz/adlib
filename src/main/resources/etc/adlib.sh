# !/bin/bash


BASEDIR=$(dirname $0)
cd $BASEDIR

java -classpath lib:lib/* com.michaelfitzmaurice.App
