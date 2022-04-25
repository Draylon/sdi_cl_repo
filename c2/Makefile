all: client server

hw.h: hw.x
	rpcgen hw.x
	
hw_svc.c hw_clnt.c hw_client.c: hw.h

hw_xdr.o: hw_xdr.c
	gcc -o hw_xdr.o -c hw_xdr.c

tools.o: tools.c
	gcc -c tools.c -o tools.o

client: hw_client.o hw_clnt.o hw_xdr.o
	gcc -o c_bolsaGeo hw_client.o hw_clnt.o hw_xdr.o -lnsl

server: hw_server.o hw_svc.o
	gcc -o s_bolsaGeo hw_server.o hw_svc.o hw_xdr.o -lnsl

.PHONY: clean

clean:
	-rm *.o
	-rm hw_xdr.c
	-rm s_bolsaGeo
	-rm c_bolsaGeo
	-rm hw.h
	-rm hw_clnt.c
	-rm hw_svc.c


build2:
	make clean
	git pull
	make