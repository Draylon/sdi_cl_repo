#include <stdio.h>
#include <rpc/rpc.h>

// Interface gerada pelo RPCGen a partir da IDL (hw.x) especificada
#include "hw.h"

// O client 1 "configura" o servidor de acordo com o arquivo servidor.in
int setServer(CLIENT *loc_cl, char *host) {
	int   *reti = NULL;

	// Lendo arquivo stdin
	char descarte[256]; // leituras p/ descarte
	int descarte_n; // leituras p/ descarte
	char peca;

	int nprat;
	int nest;
	
	int nc=0;           // número de clientes
	int np=0;           // número de pecas
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

	
	// Set npecas no servidor
	reti = specas_1(&np, loc_cl);
	if (reti == NULL) {
			clnt_perror(loc_cl,host);
			exit(1);
	}


	// Linha 4 arquivo
	if (!fscanf(stdin, "%s %s", descarte, descarte)) {
					 printf ("ERRO\n");
					 return 1;
				}
	for (int i=0; i < np; i++) {
		 if (!fscanf(stdin, "%c%c", &descarte[0], &peca)) {
							printf ("ERRO\n");
							return 1;
					 }
		 // Set peca no servidor
		 reti = speca_1(&peca, loc_cl);
		 if (reti == NULL) {
				 clnt_perror(loc_cl,host);
				 exit(1);
		 }
	}

	// linha 5 da config
	if (!fscanf(stdin, "%s %s %i", descarte, descarte, &nest)) {
						printf ("ERRO\n");
						return 1;
				}
	
	// linha 6 da config
	if (!fscanf(stdin, "%s %s", descarte, descarte)) {
					 printf ("ERRO\n");
					 return 1;
				}
	for (int i=0; i < nest; i++) {
		if (!fscanf(stdin, " %s", descarte)) {
			printf ("ERRO\n");
			return 1;
		}
	}
	// if (!fscanf(stdin, "%s", &descarte)) {
	// 	printf ("ERRO\n");
	// 	return 1;
	// }

	// linha 7 da config
	if (!fscanf(stdin, "%s %s %i", descarte, descarte, &nprat)) {
			printf ("ERRO\n");
			return 1;
	}
	
	// linha 8 da config
	if (!fscanf(stdin, "%s %s", descarte, descarte)) {
					 printf ("ERRO\n");
					 return 1;
				}
	for(int i=0;i < nprat;i++){
		if (!fscanf(stdin, "%s", descarte)) {
							printf ("ERRO\n");
							return 1;
					 }
	}

	// linha 9 da config
	if (!fscanf(stdin, '\0', descarte)) {
					 printf ("ERRO\n");
					 return 1;
				}

	// continuar a leitura do arquivo e configuração
  // ...
	// TODO
	return 0;
}

void skip_buffer(){
	char descarte[256];
	for(int i=0;i < 9;i++){
		if(!fscanf(stdin,"%s",descarte));
	}
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

struct peca_req pecareq;


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
	}else{
		skip_buffer();
	}

	// Linha 10 do arquivo
	if (!fscanf(stdin, "%s %s %s", descarte, descarte, descarte)) {
						printf ("ERRO\n");
						return 1;
				}

	char string[256];
	int user_id;
	char peca_id;
	int peca_qtd;
	struct peca_req *preq = malloc(sizeof(peca_req));
	while(true){
		if (!fscanf(stdin, "pCli%i = %c", &user_id, &peca_id)) {
			if(user_id == atoi(argv[2])){
				if (!fscanf(stdin, "QtdCli%i = %i", &user_id, &peca_qtd)) {
					preq->id=peca_id;
					preq->peca_qtd;
					solicitapeca_1(preq);
					break;
				}
			}
		}
	}
	free(preq);


	printf ("##  Cliente %d  ##\n",atoi(argv[2]));
	printf ("Status: atendido\n");
	printf ("pCli: B\n");
	printf ("###########\n");

	endclient_1(NULL, cl);
	// if (ret == NULL) {
	// 		clnt_perror(cl,argv[1]);
	// 		exit(1);
	// }

  svc_exit();
	return 0;
}
