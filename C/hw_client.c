#include <stdio.h>
#include <rpc/rpc.h>

// Interface gerada pelo RPCGen a partir da IDL (hw.x) especificada
#include "hw.h"

// O client 1 "configura" o servidor de acordo com o arquivo servidor.in
int setServer(CLIENT *loc_cl, char *host) {
	int   *reti = NULL;

	// Lendo arquivo stdin
	char descarte[256]; // leituras p/ descarte
	int nc=0;           // número de clientes
	int np=0;           // número de pecas
	char peca;
	int nesteira;
	char esteira[100];
	int nprat;
	char* prat;
	// ... demais variáveis p/ leitura do arquivo de conf.

	// Linha 1 do arquivo
	if (!fscanf(stdin, "%s %s %s", descarte, descarte, descarte)) {
						printf ("ERRO\n");
						return 1;
				}
	// Linha 2 do arquivo
	if (!fscanf(stdin, "%s %s %i", descarte, descarte, &nc)) {
						printf ("ERRO\n");
						return 1;
				}

	// Set nclientes no servidor
	reti = sclientes_1(&nc, loc_cl);
	if (reti == NULL) {
			clnt_perror(loc_cl,host);
			exit(1);
	}

	// Linha 3 arquivo
	if (!fscanf(stdin, "%s %s %i", descarte, descarte, &np)) {
						printf ("ERRO\n");
						return 1;
				}

	// Linha 4 arquivo
	if (!fscanf(stdin, "%s %s", descarte, descarte)) {
					 printf ("ERRO\n");
					 return 1;
				}
	for (int i=0; i < np; i++) {
		 if (!fscanf(stdin, "%c %c", &descarte[0], &peca)) {
							printf ("ERRO\n");
							return 1;
					 }
		 // Set peca no servidor
		 reti = specas_1(&peca, loc_cl);
		 if (reti == NULL) {
				 clnt_perror(loc_cl,host);
				 exit(1);
		 }
	}

	// linha 5 da config
	if (!fscanf(stdin, "%s %s %i", descarte, descarte, &nesteira)) {
						printf ("ERRO\n");
						return 1;
				}
	
	// linha 6 da config
	if (!fscanf(stdin, "%s %s", descarte, descarte)) {
					 printf ("ERRO\n");
					 return 1;
				}
	
	for(int i=0;i < nesteira;i++){
		if (!fscanf(stdin, "%s", &esteira)) {
							printf ("ERRO\n");
							return 1;
					 }
		struct param esteira1 = parse_esteira(esteira);
		 // Set esteira no servidor
		 reti = specas_1(&peca, loc_cl);
		 if (reti == NULL) {
				 clnt_perror(loc_cl,host);
				 exit(1);
		 }
	}

	// linha 7 da config
	if (!fscanf(stdin, "%s %s %i", descarte, descarte, &nprat)) {
						printf ("ERRO\n");
						return 1;
				}
	
	// linha 8 da config
	
	

	// continuar a leitura do arquivo e configuração
  // ...
	// TODO


	return 0;
}

struct param parse_esteira(char* str){
	struct param a;

	unsigned int cp1=1;
	char c=str[cp1];
	char num1[20];
	char num2[20];
	unsigned int np1=0;
	while(c != 'E'){
		num1[np1]=c;np1++;
		cp1++;
		c=str[cp1];
	}
	cp1++;
	c=str[cp1];
	np1=0;
	while(c != '\0'){
		num2[np1]=c;np1++;
		cp1++;
		c=str[cp1];
	}
	a.arg1 = atoi(num1);
	a.arg2 = atoi(num2);
	return a;
}

int main (int argc, char *argv[]) {
	// Estrutura RPC de comunicação
	CLIENT *cl;

	// Verificação dos parâmetros oriundos da console
	if (argc != 3) {
		printf("ERRO: ./client <hostname> <nClient>\n");
		exit(1);
	}

	// Conexão com servidor RPCargv[1]
	cl = clnt_create(argv[1], PROG, VERS, "tcp");
	if (cl == NULL) {
		clnt_pcreateerror(argv[1]);
		exit(1);
	}

	// O Cliente 1 deve configurar o servidor de acordo com o arquivo de entrada (somente o Cliente 1)

	if (atoi(argv[2]) == 1) {
		setServer(cl,argv[1]);
	}

	// ...
	// continuar o código do problema.
	// TODO


	if (atoi(argv[2]) == 1) {
		printf ("##  Cliente %d  ##\n",atoi(argv[2]));
		printf ("Status: atendido\n");
		printf ("pCli: B\n");
		printf ("###########\n");
         } else {
		printf ("##  Cliente %d  ##\n",atoi(argv[2]));
		printf ("Status: atendido\n");
		printf ("pCli: J\n");
		printf ("###########\n");
         }

	endclient_1(NULL, cl);
	// if (ret == NULL) {
	// 		clnt_perror(cl,argv[1]);
	// 		exit(1);
	// }

  svc_exit();
	return 0;
}
