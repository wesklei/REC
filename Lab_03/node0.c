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
} dt0;


/* students to write the following two routines, and maybe some others */
struct rtpkt pacote;

void rtinit0() 
{
	//faz a tabela do pacote iniciar com infinito
	pacote.mincost[0] = INFINITO;
	pacote.mincost[1] = INFINITO;
	pacote.mincost[2] = INFINITO;
	pacote.mincost[3] = INFINITO;


	printf("=>Executando rinit0()\n");

	int i,j;

	for(i=0;i<NODES;i++)
	{
		for(j=0; j<NODES; j++)
		{
			dt0.costs[i][j]= INFINITO;//inicia com infinito

		}
	}

	//agora atualiza a distancia conforme a imagem
	dt0.costs[1][1]=1;
	dt0.costs[2][2]=3;
	dt0.costs[3][3]=7;

	//atualiza a tabela do pacote com a distancia minima conhecida entre os nos
	for(i=0;i<NODES;i++)
	{
		for(j=0; j<NODES; j++)
		{
			if(pacote.mincost[i] > dt0.costs[i][j])
				pacote.mincost[i] = dt0.costs[i][j];
		}
	}
	//chama tolayer2(pacote) para cada no vizinho, repassando os dados da tabela
	pacote.sourceid = 0;//esse eh o no 0
	pacote.destid = 1; //vizinho
	tolayer2(pacote);  

	pacote.sourceid = 0;
	pacote.destid = 2;
	tolayer2(pacote);  

	pacote.sourceid = 0;
	pacote.destid = 3;
	tolayer2(pacote);  

	printdt0(&dt0);
	printf("=>Finalizou rtinit0()\n");
}


void rtupdate0(rcvdpkt)
	struct rtpkt *rcvdpkt;
{
	//faz a tabela do pacote iniciar com infinito
        pacote.mincost[0] = INFINITO;
	pacote.mincost[1] = INFINITO;
	pacote.mincost[2] = INFINITO;
	pacote.mincost[3] = INFINITO;
	
	printf("=>Executando rtupdate0()\n");

	extern float clocktime; //no documento falava para usar o extern
	printf("=>rtupdate0() %f\n recebeu do no: %d\n", clocktime, rcvdpkt->sourceid);
	printdt0(&dt0);//mostra tabela

	
	int flag = 0;
	int j,i;
	for(i = 0; i < NODES; ++i)
	{
	       // usa flag para marcar q teve alteracao na tabela
	       // teste se o custo alterou
		if(dt0.costs[i][rcvdpkt->sourceid] > rcvdpkt->mincost[i] +  dt0.costs[rcvdpkt->sourceid][rcvdpkt->sourceid])
		{
			dt0.costs[i][rcvdpkt->sourceid] = rcvdpkt->mincost[i]  + dt0.costs[rcvdpkt->sourceid][rcvdpkt->sourceid]; 
			flag = 1;
		}
	}
	//se teve alteracao, atualiza o pacote
	if(flag)
	{
		for(i=0;i<NODES;i++)
		{
			for(j=0; j<NODES; j++)
			{
				if(pacote.mincost[i]  > dt0.costs[i][j]) 
					pacote.mincost[i] = dt0.costs[i][j];
			}
		}

		//chama tolayer2(pacote) para cada no vizinho, repassando os dados da tabela
		pacote.sourceid = 0;
		pacote.destid = 1;
		tolayer2(pacote);

		pacote.sourceid = 0;
		pacote.destid = 2;
		tolayer2(pacote);

		pacote.sourceid = 0;
		pacote.destid = 3;
		tolayer2(pacote);
		
		printf("=>Teve alteracao na tabela!\n");
		//mostra a nova tabela
		printdt0(&dt0);
	}
}


printdt0(dtptr)
	struct distance_table *dtptr;

{
	printf("                via     \n");
	printf("   D0 |    1     2    3 \n");
	printf("  ----|-----------------\n");
	printf("     1|  %3d   %3d   %3d\n",dtptr->costs[1][1],
			dtptr->costs[1][2],dtptr->costs[1][3]);
	printf("dest 2|  %3d   %3d   %3d\n",dtptr->costs[2][1],
			dtptr->costs[2][2],dtptr->costs[2][3]);
	printf("     3|  %3d   %3d   %3d\n",dtptr->costs[3][1],
			dtptr->costs[3][2],dtptr->costs[3][3]);
}

linkhandler0(linkid, newcost)   
	int linkid, newcost;

	/* called when cost from 0 to linkid changes from current value to newcost*/
	/* You can leave this routine empty if you're an undergrad. If you want */
	/* to use this routine, you'll need to change the value of the LINKCHANGE */
	/* constant definition in prog3.c from 0 to 1 */

{
}

