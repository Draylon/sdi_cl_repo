# GNU Makefile
JAR=/usr/local/jdk1.8.0_131/bin/jar
JAVA=/usr/local/jdk1.8.0_131/bin/java
JAVAC=/usr/local/jdk1.8.0_131/bin/javac

JFLAGS = -g
.SUFFIXES: MYC/.java .class
.java.class:
	$(JAVAC) $(JFLAGS) MYC\$*.java

default: Client ServerPublisher WSPandeloServerImpl WSPandeloServer WSCortesServerImpl WSCortesServer WSCoberturaServerImpl WSCoberturaServer WSRecheioServerImpl WSRecheioServer WSRecepcaoServerImpl WSRecepcaoServer

Client: MYC/Client.java
	$(JAVAC) $(JFLAGS) MYC/Client.java

WSRecepcaoServerImpl: MYC/WSRecepcaoServerImpl.java
	$(JAVAC) $(JFLAGS) MYC/WSRecepcaoServerImpl.java

WSRecepcaoServer: MYC/WSRecepcaoServer.java
	$(JAVAC) $(JFLAGS) MYC/WSRecepcaoServer.java

WSCortesServerImpl: MYC/WSCortesServerImpl.java
	$(JAVAC) $(JFLAGS) MYC/WSCortesServerImpl.java

WSCortesServer: MYC/WSCortesServer.java
	$(JAVAC) $(JFLAGS) MYC/WSCortesServer.java

WSPandeloServerImpl: MYC/WSPandeloServerImpl.java
	$(JAVAC) $(JFLAGS) MYC/WSPandeloServerImpl.java

WSPandeloServer: MYC/WSPandeloServer.java
	$(JAVAC) $(JFLAGS) MYC/WSPandeloServer.java

WSRecheioServerImpl: MYC/WSRecheioServerImpl.java
	$(JAVAC) $(JFLAGS) MYC/WSRecheioServerImpl.java

WSRecheioServer: MYC/WSRecheioServer.java
	$(JAVAC) $(JFLAGS) MYC/WSRecheioServer.java

WSCoberturaServerImpl: MYC/WSCoberturaServerImpl.java
	$(JAVAC) $(JFLAGS) MYC/WSCoberturaServerImpl.java

WSCoberturaServer: MYC/WSCoberturaServer.java
	$(JAVAC) $(JFLAGS) MYC/WSCoberturaServer.java

ServerPublisher: MYC/ServerPublisher.java
	$(JAVAC) $(JFLAGS) MYC/ServerPublisher.java

clean:
	rm -f MYC/*.class

build2:
	git pull origin master
	#!/bin/bash
    nro_proc=`grep Nro_clientes ambiente.in | cut -d " " -f 2`
    make clean > /dev/null
    make > /dev/null
    java MYC.ServerPublisher ens1 < stdin0 &
    for i in `seq 1 $nro_proc`
    do
        sleep 1
        pos=`expr $i + 1`;
        nom_maquinas=`grep Maquinas ambiente.in | cut -d " " -f $pos`
        px=`grep Processos ambiente.in | cut -d " " -f $pos`
        #scp *.class $nom_maquinas: > /dev/null
        scp -r MYC $nom_maquinas: > /dev/null
        scp stdin0 $nom_maquinas: > /dev/null
        #echo 'ssh' $nom_maquinas 'java MYC.'$px ens1 $i
        ssh $nom_maquinas java MYC.$px ens1 $i < stdin0
        #java $px ens1 $i < stdin0
        #echo 'ssh' $nom_maquinas 'rm -f' $px'.class'
        ssh $nom_maquinas rm -rf MYC >/dev/null
        ssh $nom_maquinas rm -f stdin0 >/dev/null
    done
