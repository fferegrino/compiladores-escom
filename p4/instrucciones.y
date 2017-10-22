%{
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>

#define PI 3.141592
extern int yylex(void);
extern char* yytext;
extern int nlines;
int yyerror(char *s);
float toRadians(float grados);
float varTan(float grados);
void imprime(float resultado);
void imprime_invalido();
short int valores=1;
extern FILE * yyin;
FILE * fsalida;
%}

%union{
	float real;
}

%token <real> TKN_NUM
%type <real> expresion
%token TKN_PTOCOMA
%token TKN_MAS
%token TKN_MENOS
%token TKN_MULT
%token TKN_MOD
%token TKN_RAIZ
%token TKN_DIV
%token TKN_POW
%token TKN_PA
%token TKN_PC
%token TKN_SEN
%token TKN_COS
%token TKN_TAN
%left TKN_MAS TKN_MENOS TKN_MOD
%left TKN_MULT TKN_DIV
%left TKN_SEN TKN_COS TKN_TAN 
%left TKN_POW
//%left TKN_PA TKN_PC
%left TKN_RAIZ
%start instrucciones
%%

instrucciones : instrucciones calculadora
		| calculadora
		;
		
calculadora	: expresion TKN_PTOCOMA 
			{
				if(valores > 0){
					imprime($1);
				}
				else
				{
					imprime_invalido();
					valores = 1;
				}
			}
			;

expresion	: TKN_NUM { $$ = $1; }
			| TKN_PA expresion TKN_PC { $$ = $2; }
			| expresion TKN_MAS expresion { $$ = $1 + $3; }
			| expresion TKN_MENOS expresion { $$ = $1 - $3; }
			| expresion TKN_MULT expresion { $$ = $1 * $3; }			
			| expresion TKN_POW expresion { $$ = pow($1,$3); }
			| expresion TKN_MOD expresion { $$ = fmod($1,$3); }
			| TKN_RAIZ expresion 
			{
				if($2 < 0)
				{
					valores = 0;
				}
				else
				{
					$$ = sqrt($2);
					valores = 1;
				}
			}
			| expresion TKN_DIV expresion
			{
				if($3 == 0)
				{
					valores = 0;
				}
				else
				{
					$$ = $1 / $3;
					valores = 1;
				}
			}
			| TKN_SEN expresion { $$ = sin(toRadians($2)); }
			| TKN_COS expresion { $$ = cos(toRadians($2)); }
			| TKN_TAN expresion 
			{
				if(-0.000001 < cos(toRadians($2)) && cos(toRadians($2)) < 0.000001)
				{
					valores = 0;
				}
				else
				{
					$$ = tan(toRadians($2));
					valores = 1;
				
				}
			}
			;

%%
int yyerror(char *s)
{
	printf("%s\n", s);
}


float toRadians(float grados)
{
	return grados*(3.1415926535/180);
}

float varTan(float grados){
	float t = toRadians(grados);
	return cos(t);
}

void imprime(float resultado)
{
	fprintf(fsalida,"Resultado %5.5f\n",resultado);
}

void imprime_invalido()
{
	fprintf(fsalida,"Valor indefinido\n");
}


int main(int argc, char **argv)
{
	if(argc > 2)
	{
		yyin = fopen(argv[1],"r");
		fsalida = fopen(argv[2], "w");
	}
	else
	{
		printf("Forma de uso: ./salida archivo_entrada archivo_salida\n");
		return 0;
	}
	/*Acciones a ejecutar antes del analisis*/
	yyparse();
	/*Acciones a ejecutar despues del analisis*/
	
	return 0;
}
