#! /bin/bash

# start DB
DB_PORT=5555

if [ `uname` == "Linux" ]; then
    DB_DIR="/mnt/MYUSB/NGenDB/DATA"
    dbcmd="sudo /opt/mongo/bin/mongod --dbpath $DB_DIR --port $DB_PORT --fork --logpath `pwd`/mongodb.log"
elif [ `uname` == "Darwin" ]; then
    DB_DIR="/Volumes/MYUSB/NGenDB/DATA"
    dbcmd="`pwd`/db/mongod --dbpath $DB_DIR --port $DB_PORT --fork --logpath `pwd`/mongodb.log"
fi

nohup $dbcmd &

sleep 10s

nohup ant run &