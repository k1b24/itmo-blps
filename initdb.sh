# setup db
sudo -u postgres psql -d kachalka-backend -f ./sper-bank/src/main/resources/sql/init.sql
sudo -u postgres psql -d sper-bank -f ./kachalka-backend/src/main/resources/sql/init.sql