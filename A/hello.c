#include<stdio.h>

int main () {
  char letra;
  int nroLetras=0;

  if (!fscanf(stdin, "%i", &nroLetras)) {
    printf ("ERRO\n");
    return 1;
  } else {
    printf ("Nro: %d\n", nroLetras);
    for (int i=0; i < nroLetras; i++) {
      if (fscanf(stdin, "%c", &letra))
        printf ("%c", letra);
    }
    printf("\n");
  }
  return 0;
}
