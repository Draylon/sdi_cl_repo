# GNU Makefile
JAR=/usr/local/jdk1.8.0_131/bin/jar
JAVA=/usr/local/jdk1.8.0_131/bin/java
JAVAC=/usr/local/jdk1.8.0_131/bin/javac

JFLAGS = -g
.SUFFIXES: MYC/.java .class
.java.class:
	$(JAVAC) $(JFLAGS) MYC\$*.java

default: Client ServerPublisher WSPandeloServerImpl WSPandeloServer WSCortesServerImpl WSCortesServer WSCoberturaServerImpl WSCoberturaServer WSRecheioServerImpl WSRecheioServer WSRecepcaoServerImpl WSRecepcaoServer

Client: #MYC/Client.java
	$(JAVAC) $(JFLAGS) MYC/Client.java

WSRecepcaoServerImpl: #MYC/WSRecepcaoServerImpl.java
	$(JAVAC) $(JFLAGS) MYC/WSRecepcaoServerImpl.java

WSRecepcaoServer: #MYC/WSRecepcaoServer.java
	$(JAVAC) $(JFLAGS) MYC/WSRecepcaoServer.java

WSCortesServerImpl: #MYC/WSCortesServerImpl.java
	$(JAVAC) $(JFLAGS) MYC/WSCortesServerImpl.java

WSCortesServer: #MYC/WSCortesServer.java
	$(JAVAC) $(JFLAGS) MYC/WSCortesServer.java

WSPandeloServerImpl: #MYC/WSPandeloServerImpl.java
	$(JAVAC) $(JFLAGS) MYC/WSPandeloServerImpl.java

WSPandeloServer: #MYC/WSPandeloServer.java
	$(JAVAC) $(JFLAGS) MYC/WSPandeloServer.java

WSRecheioServerImpl: #MYC/WSRecheioServerImpl.java
	$(JAVAC) $(JFLAGS) MYC/WSRecheioServerImpl.java

WSRecheioServer: #MYC/WSRecheioServer.java
	$(JAVAC) $(JFLAGS) MYC/WSRecheioServer.java

WSCoberturaServerImpl: #MYC/WSCoberturaServerImpl.java
	$(JAVAC) $(JFLAGS) MYC/WSCoberturaServerImpl.java

WSCoberturaServer: #MYC/WSCoberturaServer.java
	$(JAVAC) $(JFLAGS) MYC/WSCoberturaServer.java

ServerPublisher: #MYC/ServerPublisher.java
	$(JAVAC) $(JFLAGS) MYC/ServerPublisher.java

clean:
	rm -f MYC/*.class
