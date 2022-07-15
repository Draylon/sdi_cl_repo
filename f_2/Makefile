# GNU Makefile
MVN=/usr/bin/mvn
JAR=/usr/local/jdk1.8.0_131/bin/jar
JAVA=/usr/local/jdk1.8.0_131/bin/java
JAVAC=/usr/local/jdk1.8.0_131/bin/javac

NAME=FileDESC
cGROUP=com
nGROUP=wsudesc
GROUP=$(cGROUP).$(nGROUP)
MVNFLAGS=archetype:generate -DgroupId=$(GROUP) -DartifactId=$(NAME) -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
MVNDEP=dependency:get -Dartifact=com.dropbox.core:dropbox-core-sdk:4.0.1

JFLAGS = -g
.SUFFIXES: .java .class
.java.class:
		$(JAVAC) $(JFLAGS) $*.java

default: DESCMon newproject makepom makedep makelink sourcejava makepackage allOK

DESCCli: DESCCli.java dep.xml

DESCMon: DESCMon.java
	$(JAVAC) DESCMon.java

CLASSES = \
	 DESCMon.java

classes: $(CLASSES:.java=.class)

newproject:
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	@echo "%%%%                     SDIDB: Criação do Projeto MAVEN"
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	$(MVN) $(MVNFLAGS)

makepom:
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	@echo "%%%%                     SDIDB: Copia do pom.xml"
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	head -16 $(NAME)/pom.xml > /tmp/pom.xml
	cat dep.xml >> /tmp/pom.xml
	mv /tmp/pom.xml $(NAME)/pom.xml

makedep:
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	@echo "%%%%                     SDIDB: download das dependências"
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	cd $(NAME) > /dev/null && $(MVN) $(MVNDEP)

makelink:
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	@echo "%%%%                     SDIDB: Link p/ dependências"
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	cd $(NAME) > /dev/null && ln -sf ~/.m2/repository/com com
	@#cd $(NAME) && cp -r ~/.m2/repository/com .

sourcejava:
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	@echo "%%%%                     SDIDB: Link arquivo fonte (DESCCli.java)"
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	cd $(NAME)/src/main/java/$(cGROUP)/$(nGROUP)/ > /dev/null &&  ln -sf ../../../../../../DESCCli.java DESCCli.java &&  ln -sf ../../../../../../DropboxConfig.java DropboxConfig.java


makepackage:
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	@echo "%%%%                     SDIDB: Compila projeto MAVEN"
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	cd $(NAME) >> /dev/null && $(MVN) package

allOK:
	@mkdir output
	@mkdir tempfiles
	@mkdir input
	@wget --no-check-certificate https://132.226.161.239/auditor.tgz > /dev/null 2>&1
	@wget --no-check-certificate https://132.226.161.239/inputfile.tgz > /dev/null 2>&1
	@wget --no-check-certificate https://132.226.161.239/tempfiles.tgz > /dev/null 2>&1
	@tar xf auditor.tgz > /dev/null 2>&1
	@cd input > /dev/null 2>&1; tar xzf ../inputfile.tgz > /dev/null 2>&1; cd -
	@ rm -f inputfile.tgz
	@cd tempfiles > /dev/null 2>&1; tar xzf ../tempfiles.tgz	> /dev/null 2>&1; cd -
	@rm -f tempfiles.tgz
	@cp input/arqbla1.map output/.
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	@echo "%%%%                     SDIDB: Para testar HelloJar, execute no terminal:	"
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	@echo "cd $(NAME) ; java -cp  target/sdi-dropbox-1.0-SNAPSHOT.jar com.wsudesc.App ; cd -"
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	@echo "%%%%                     SDIDB: Para testar UploadDB (/tmp/test.txt), execute no terminal:	"
	@echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
	@echo "cd $(NAME) ; java -cp  target/$(NAME)-1.0-SNAPSHOT.jar:com/dropbox/core/dropbox-core-sdk/4.0.1/dropbox-core-sdk-4.0.1.jar:com/fasterxml/jackson/core/jackson-core/2.7.4/jackson-core-2.7.4.jar $(cGROUP).$(nGROUP).app.DESCCli;  cd -"


cleanall:
	rm -rf $(NAME)
	rm -rf ~/.m2/

clean:
	rm -rf $(NAME)
	rm -rf output/
	rm -rf tempfiles/
	rm -rf input/
	rm -rf _auditor_
	rm -f auditor.*
	rm -f tempfiles.*
	rm -f inputfile.*
	rm -f Logtrans.log
	rm -f *.class
