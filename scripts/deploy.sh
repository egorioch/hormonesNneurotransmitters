#!/usr/bin/env bash

mvn clean compile package

echo 'Copy files...'

#scp -- безопасное копирование поверх ssh
#после айпишника указываем адрес, куда кладём jar'ник
scp -i ~/.ssh/id_rsa.pub \
  target/hormonesNneurotransmitters-1.0-SNAPSHOT.jar \
  seesoon21@195.133.146.1:/home/seesoon21/

echo 'Restart server...'
#eof -- указывает, что мы будем передавать блок данных
#pgrep -- получает id процесса, xargs его забирает и заканчивает
# & -- знак, говорящий о том, что процесс уйдёт в фон после команды
ssh -i ~/.ssh/id_rsa.pub seesoon21@195.133.146.1 << EOF

pgrep java | xargs kill -9
nohup java -jar hormonesNneurotransmitters-1.0-SNAPSHOT.jar > log.txt &

EOF

echo 'Bye...'
