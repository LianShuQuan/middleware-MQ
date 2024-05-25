#!/bin/bash
cd /root/OOMALL
git pull
docker exec -it $(docker container ls -aq -f name=mongo.*) mongorestore -u demouser -p 123456 --authenticationDatabase oomall -d oomall --dir /mongo/oomall --drop --preserveUUID