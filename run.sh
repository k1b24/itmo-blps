#bin/bash

# setup db
psql -h localhost -d kachalka kachalka_user -f ./sper-bank/src/main/resources/sql/init.sql
psql -h localhost -d kachalka kachalka_user -f ./kachalka-backend/src/main/resources/sql/init.sql

#build jars
gradle clean
gradle build
gradle bootRun --parallel --max-workers=4