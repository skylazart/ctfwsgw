#!/bin/bash

# Script para gerenciar a inicializacao do CTF GATEWAY
# Autor: Felipe Cerqueira - felipe.cerqueira@druid.com.br
# Data: Junho 2017
# Copyright(C) DRUID 2012,2013,2014,2015,2016,2017

export JAVA_HOME=/home/ctfwsgw/ctfwsgw/jdk/jdk1.8.0_131/
export PATH=$JAVA_HOME/bin:$PATH


PIDFILE=../run/ctfwsgw.pid

start() {
        echo "Iniciando CTF GATEWAY..."
	    rm nohup.out > /dev/null 2> /dev/null

        nohup java \
                -d64 -server -Xmx1g -Xms1g -Xss512k \
                -jar -Dlog4j.configuration=file:../etc/log4j2.xml \
		./ctfwsgw.jar ../etc/configuration.properties &

        pid=$!
        echo "CTF GATEWAY executando. PID = $pid"
        echo $pid > $PIDFILE

	sleep 1
	cat nohup.out
}

stop() {
        test -f $PIDFILE
        if [ "$?" != "0" ]; then
                echo "CTF GATEWAY nao esta executando"
                exit 1
        fi

        pid=`cat $PIDFILE`
        echo "Parando CTF GATEWAY pid $pid..."
        kill $pid > /dev/null 2> /dev/null
        echo "Resultado ($?)"
        rm $PIDFILE
}

restart() {
        stop;
        sleep 2
        start;
}

echo "CTF GATEWAY TIM (C) Copyright 2012 Druid Internet Systems"

id | grep root > /dev/null 2> /dev/null
if [ "$?" = "0" ]; then
        echo "Nao execute o CTF GATEWAY como root!!"
        echo "O programa nao iniciou. Faca sudo para o usuario ctfusr e tente novamente."
        exit 1
fi

case "$1" in
'start')
        start;
        ;;
'stop')
        stop;
        ;;
'restart')
        restart;
        ;;
*)
        echo "Use: $0 start|stop|restart"
esac

