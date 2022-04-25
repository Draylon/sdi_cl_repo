#include <rpc/rpc.h>
#include <unistd.h>

// Interface gerada pelo RPCGen a partir da IDL (hw.x) especificada
#include "hw.h"

int nclientes = 0;
int npecas = 0;
int np_ct = 0;
char *pecas;

int nentr;
char *entregas;

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
// Define quantidade de peças no estoque
int *specas_1_svc(int *a, struct svc_req *req) {
     static int ret = 0;
     npecas = *a;
     
     pecas = malloc(sizeof(char)*npecas);
     entregas = malloc(sizeof(char)*npecas);
     if(pecas == NULL) ret=1;
     
     return (&ret);
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Inclui uma peça no estoque
int *speca_1_svc(char *a, struct svc_req *req) {
     static int ret = 0;
     pecas[np_ct] = *a;
     np_ct++;
     return (&ret);
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Configura o número total de clientes
int *sclientes_1_svc(int *a, struct svc_req *req) {
     static int ret = 10;
     nclientes = *a;
     return (&ret);
}


//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Cliente foi atendido com sucesso
int *solicitapeca_1_svc(struct peca_req *pecareq,struct svc_req *req){
     static int ret = 10;
     // pecareq->id;
     // pecareq->qt;
     char id = pecareq->id;
     int qt = pecareq->qt;
     for(int q1 = 0;q1 < qt;q1++){
          for(int i=0;i<npecas;i++){
               if(pecas[i] == id){
                    pecas[i] = (char)0;
                    
                    entregas[nentr] = id;
                    nentr++;
     }}}
     return (&ret);
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Cliente foi atendido com sucesso
void *endclient_1_svc(void *a, struct svc_req *req) {
     if (--nclientes == 0) {
     printf ("##  Servidor  ##\n");
     printf ("Status: finalizado\n");
               
               for (int step = 0; step < npecas - 1; ++step) {
                    for (int i = 0; i < npecas - step - 1; ++i) {
                         if ((int)pecas[i] > (int)pecas[i + 1]) {
                              char temp = pecas[i];
                              pecas[i] = pecas[i + 1];
                              pecas[i + 1] = temp;
                         }
                    }
               }
               for (int step = 0; step < nentr - 1; ++step) {
                    for (int i = 0; i < nentr - step - 1; ++i) {
                         if ((int)entregas[i] > (int)entregas[i + 1]) {
                              char temp = pecas[i];
                              entregas[i] = entregas[i + 1];
                              entregas[i + 1] = temp;
                         }
                    }
               }

               printf ("estoque: ");
               for (int i = 0; i < npecas; ++i) {
                    printf("%i ", pecas[i]);
               }
               
               printf ("entregas: ");
               for (int i = 0; i < nentr; ++i) {
                    printf("%i ", entregas[i]);
               }

               printf("###########\n");
               

               //printf ("estoque: A A B B E H H H H J\n");
               //printf ("entregas: B B J \n");
               exit(0);
     }
}

