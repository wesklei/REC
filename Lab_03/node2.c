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
} dt2;


/* students to write the following two routines, and maybe some others */
struct rtpkt pacote;

void rtinit2() 
{
	//faz a tabela do pacote iniciar com infinito
	pacote.mincost[0] = INFINITO;
	pacote.mincost[1] = INFINITO;
	pacote.mincost[2] = INFINITO;
	pacote.mincost[3] = INFINITO;


	printf("=>Executando rinit2()\n");

	int i,j;

	for(i=0;i<NODES;i++)
	{
		for(j=0; j<NODES; j++)
		{
			dt2.costs[i][j]= INFINITO;//inicia com infinito

		}
	}

	//agora atualiza a distancia conforme a imagem
	dt2.costs[0][0]=3;
	dt2.costs[1][1]=1;
	dt2.costs[2][2]=INFINITO;
	dt2.costs[3][3]=2;

	//atualiza a tabela do pacote com a distancia minima conhecida entre os nos
	for(i=0;i<NODES;i++)
	{
		for(j=0; j<NODES; j++)
		{
			if(pacote.mincost[i] > dt2.costs[i][j])
				pacote.mincost[i] = dt2.costs[i][j];
		}
	}
	//chama tolayer2(pacote) para cada no vizinho, repassando os dados da tabela
	pacote.sourceid = 2;//esse eh o no 2
	pacote.destid = 0; //vizinho
	tolayer2(pacote);  

	pacote.sourceid = 2;
	pacote.destid = 1;
	tolayer2(pacote);  

	pacote.sourceid = 2;
	pacote.destid = 3;
	tolayer2(pacote);  

	printdt2(&dt2);
	printf("=>Finalizou rtinit2()\n");

}


void rtupdate2(rcvdpkt)
  struct rtpkt *rcvdpkt;
  
{
	//faz a tabela do pacote iniciar com infinito
        pacote.mincost[0] = INFINITO;
	pacote.mincost[1] = INFINITO;
	pacote.mincost[2] = INFINITO;
	pacote.mincost[3] = INFINITO;
	
	printf("=>Executando rtupdate2()\n");

	extern float clocktime; //no documento falava para usar o extern
	printf("=>rtupdate2() %f\n recebeu do no: %d\n", clocktime, rcvdpkt->sourceid);
	printdt2(&dt2);//mostra tabela

	
	int flag = 0;
	int j,i;
	for(i = 0; i < NODES; ++i)
	{
	       // usa flag para marcar q teve alteracao
	       // teste se o custo alterou
		if(dt2.costs[i][rcvdpkt->sourceid] > rcvdpkt->mincost[i] +  dt2.costs[rcvdpkt->sourceid][rcvdpkt->sourceid])
		{
			dt2.costs[i][rcvdpkt->sourceid] = rcvdpkt->mincost[i]  + dt2.costs[rcvdpkt->sourceid][rcvdpkt->sourceid]; 
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
				if(pacote.mincost[i]  > dt2.costs[i][j]) 
					pacote.mincost[i] = dt2.costs[i][j];
			}
		}

		//chama tolayer2(pacote) para cada no vizinho, repassando os dados da tabela
		pacote.sourceid = 2;
		pacote.destid = 0;
		tolayer2(pacote);

		pacote.sourceid = 2;
		pacote.destid = 1;
		tolayer2(pacote);

		pacote.sourceid = 2;
		pacote.destid = 3;
		tolayer2(pacote);
		
		printf("=>Teve alteracao na tabela!\n");
		//mostra a nova tabela
		printdt2(&dt2);
	}
}


printdt2(dtptr)
  struct distance_table *dtptr;
  
{
  printf("                via     \n");
  printf("   D2 |    0     1    3 \n");
  printf("  ----|-----------------\n");
  printf("     0|  %3d   %3d   %3d\n",dtptr->costs[0][0],
	 dtptr->costs[0][1],dtptr->costs[0][3]);
  printf("dest 1|  %3d   %3d   %3d\n",dtptr->costs[1][0],
	 dtptr->costs[1][1],dtptr->costs[1][3]);
  printf("     3|  %3d   %3d   %3d\n",dtptr->costs[3][0],
	 dtptr->costs[3][1],dtptr->costs[3][3]);
}







