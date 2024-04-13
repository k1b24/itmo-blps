#bin/bash

#build jars
gradle clean
gradle build
gradle bootRun --parallel --max-workers=4