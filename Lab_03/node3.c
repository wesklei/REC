#include <stdio.h>

#define INFINITO 99999999
#define NODES 4

extern struct rtpkt {
	int sourceid;       /* id of sending router sending this pkt */
	int destid;         /* id of router to which pkt being sent 
			       (must be an immediate neighbor) */
	int mincost[4];    /* min cost to node 0 ... 3 */
};

extern int TRACE;
extern int YES;
extern int NO;

struct distance_table 
{
	int costs[4][4];
} dt3;

/* students to write the following two routines, and maybe some others */
struct rtpkt pacote;

void rtinit3() 
{

	//faz a tabela do pacote iniciar com infinito
	pacote.mincost[0] = INFINITO;
	pacote.mincost[1] = INFINITO;
	pacote.mincost[2] = INFINITO;
	pacote.mincost[3] = INFINITO;


	printf("=>Executando rinit3()\n");

	int i,j;

	for(i=0;i<NODES;i++)
	{
		for(j=0; j<NODES; j++)
		{
			dt3.costs[i][j]= INFINITO;//inicia com infinito

		}
	}

	//agora atualiza a distancia conforme a imagem
	dt3.costs[0][0]=7;
	dt3.costs[2][2]=2;
	dt3.costs[3][3]=INFINITO;
	//atualiza a tabela do pacote com a distancia minima conhecida entre os nos
	for(i=0;i<NODES;i++)
	{
		for(j=0; j<NODES; j++)
		{
			if(pacote.mincost[i] > dt3.costs[i][j])
				pacote.mincost[i] = dt3.costs[i][j];
		}
	}
	//chama tolayer2(pacote) para cada no vizinho, repassando os dados da tabela
	pacote.sourceid = 3;//esse eh o no 3
	pacote.destid = 0; //vizinho
	tolayer2(pacote);  

	pacote.sourceid = 3;
	pacote.destid = 2;
	tolayer2(pacote);  

	printdt3(&dt3);
	printf("=>Finalizou rtinit3()\n");
}


void rtupdate3(rcvdpkt)
	struct rtpkt *rcvdpkt;

{
	//faz a tabela do pacote iniciar com infinito
        pacote.mincost[0] = INFINITO;
	pacote.mincost[1] = INFINITO;
	pacote.mincost[2] = INFINITO;
	pacote.mincost[3] = INFINITO;
	
	printf("=>Executando rtupdate3()\n");

	extern float clocktime; //no documento falava para usar o extern
	printf("=>rtupdate3() %f\n recebeu do no: %d\n", clocktime, rcvdpkt->sourceid);
	printdt3(&dt3);//mostra tabela

	
	int flag = 0;
	int j,i;
	for(i = 0; i < NODES; ++i)
	{
	       // usa flag para marcar q teve alteracao
	       // teste se o custo alterou
		if(dt3.costs[i][rcvdpkt->sourceid] > rcvdpkt->mincost[i] +  dt3.costs[rcvdpkt->sourceid][rcvdpkt->sourceid])
		{
			dt3.costs[i][rcvdpkt->sourceid] = rcvdpkt->mincost[i]  + dt3.costs[rcvdpkt->sourceid][rcvdpkt->sourceid]; 
			flag = 1;
		}
	}
	//se teve alteracao
	if(flag)
	{
		for(i=0;i<NODES;i++)
		{
			for(j=0; j<NODES; j++)
			{
				if(pacote.mincost[i]  > dt3.costs[i][j]) 
					pacote.mincost[i] = dt3.costs[i][j];
			}
		}

		//chama tolayer2(pacote) para cada no vizinho, repassando os dados da tabela
		pacote.sourceid = 3;
		pacote.destid = 0;
		tolayer2(pacote);

		pacote.sourceid = 3;
		pacote.destid = 2;
		tolayer2(pacote);	

		printf("=>Teve alteracao na tabela!\n");
		//mostra a nova tabela
		printdt3(&dt3);
	}
}


printdt3(dtptr)
	struct distance_table *dtptr;

{
	printf("             via     \n");
	printf("   D3 |    0     2 \n");
	printf("  ----|-----------\n");
	printf("     0|  %3d   %3d\n",dtptr->costs[0][0], dtptr->costs[0][2]);
	printf("dest 1|  %3d   %3d\n",dtptr->costs[1][0], dtptr->costs[1][2]);
	printf("     2|  %3d   %3d\n",dtptr->costs[2][0], dtptr->costs[2][2]);

}







