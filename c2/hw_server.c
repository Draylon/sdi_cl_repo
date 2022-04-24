#include <rpc/rpc.h>
#include <unistd.h>

// Interface gerada pelo RPCGen a partir da IDL (hw.x) especificada
#include "hw.h"

int nclientes = 0;
int npecas = 0;
int np_ct = 0;
//char pecas[2048];
char *pecas;

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
char **func0_1_svc(void *a, struct svc_req *req) {
	static char msg[256];
	static char *p;

	printf("FUNC0 (sem parâmetros)\n");
	strcpy(msg, "Hello Func0!");
	p = msg;

	return(&p);
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
int *func1_1_svc(char **a, struct svc_req *req) {
    static int ret = 1;
    printf ("FUNC1 (%s)\n", *a);
    return (&ret);
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
int *func2_1_svc(int *a, struct svc_req *req) {
     static int ret = 10;

     printf ("FUNC2 (%d)\n", *a);
     return (&ret);

}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
int *func3_1_svc(struct param *a, struct svc_req *req) {
     static int ret=0;

     printf ("FUNC3 (%d/%d)\n", a->arg1, a->arg2);
     ret = a->arg1 * a->arg2;
     return (&ret);
}


//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Inclui uma peça no estoque
int *specas_1_svc(int *a, struct svc_req *req) {
     static int ret = 0;

     npecas = *a;
     return (&ret);
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Define quantidade de peças no estoque
int *speca_1_svc(char *a, struct svc_req *req) {
     static int ret = 0;

     pecas[np_ct] = *a;
     np_ct++;

     pecas = malloc(sizeof(char)*np_ct);
     return (&ret);
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Configura o número total de clientes
int *sclientes_1_svc(int *a, struct svc_req *req) {
     static int ret = 10;
     nclientes = *a;
     return (&ret);
}

int solicitapeca_1_svc(struct peca_req *r,struct svc_req *req){
     r->id;
     pecareq->qt;
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Cliente foi atendido com sucesso
void *endclient_1_svc(void *a, struct svc_req *req) {
     if (--nclientes == 0) {
     printf ("##  Servidor  ##\n");
               printf ("Status: finalizado\n");
               printf ("estoque: A A B B E H H H H J\n");
               printf ("entregas: B B J \n");
               exit(0);
     }
}
