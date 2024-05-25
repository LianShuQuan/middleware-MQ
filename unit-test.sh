docker rm $(docker ps -a -q)
docker exec -i $(docker container ls -aq -f name=test.*) bash /OOMALL/runtest.sh 192.168.0.48 ****