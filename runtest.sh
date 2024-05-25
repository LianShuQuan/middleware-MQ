#!/bin/bash
# recursive upload files
function traverse() {
    for file in $(ls $1)
    do
        if [ -d "$1/$file" ]; then
          curl -X MKCOL $2/$file/ -u mingqiu:$3
          traverse "$1/$file" "$2/$file" "$3"
        else
          #echo "curl -T $file $2/$file -u mingqiu:$3"
          curl -T $1/$file $2/$file -u mingqiu:$3
        fi
    done
}

## 将文件结尾从CRLF改为LF，解决了cd 错误问题
#$1为目录名
TIME=$(date "+%Y-%m-%d-%H-%M-%S")

cd /OOMALL
git pull

cp pom.bak.xml pom.xml
mvn clean install
mvn surefire-report:report > core.log
mvn jacoco:report

#echo "curl -X MKCOL  $1/test/unit-test/$TIME/ -u mingqiu:$2"
curl -X MKCOL  $1/test/unit-test/$TIME/ -u mingqiu:$2
#echo "curl -T core.log  $1/test/unit-test/$TIME/core.log -u mingqiu:$2"
curl -T core.log $1/test/unit-test/$TIME/core.log -u mingqiu:$2

#docker exec redis redis-cli -a 123456 flushdb

cd core
curl -X MKCOL  $1/test/unit-test/$TIME/core/ -u mingqiu:$2
curl -X MKCOL  $1/test/unit-test/$TIME/images/ -u mingqiu:$2
curl -X MKCOL  $1/test/unit-test/$TIME/css/ -u mingqiu:$2
traverse "/OOMALL/site/images"  "$1/test/unit-test/$TIME/images" "$2"
traverse "/OOMALL/site/css"  "$1/test/unit-test/$TIME/css" "$2"

traverse "target/site/jacoco" "$1/test/unit-test/$TIME/core" "$2"
curl -T target/site/surefire-report.html $1/test/unit-test/$TIME/core-test.html -u mingqiu:$2

cd /OOMALL
git restore pom.xml

module=("product" "region" "sfexpress" "wechatpay" "jtexpress" "alipay" "freight" "ztoexpress" "payment" "shop")
length=${#module[@]}
#echo "length =" $length
for ((i=1;i<=$length;i++))
do
  index=$(($RANDOM%$length))
  #echo "index =" $index
  M=${module[$index]}
  echo $M "testing............"
  cd /OOMALL/$M
  mvn clean surefire-report:report > $M.log
  mvn jacoco:report
  #echo "curl -X MKCOL  $1/test/unit-test/$TIME/$M/ -u mingqiu:$2"
  curl -X MKCOL  $1/test/unit-test/$TIME/$M/ -u mingqiu:$2
  traverse "target/site/jacoco" "$1/test/unit-test/$TIME/$M" "$2"
  curl -T target/site/surefire-report.html $1/test/unit-test/$TIME/$M-test.html -u mingqiu:$2
  curl -T $M.log $1/test/unit-test/$TIME/$M.log -u mingqiu:$2
done

