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

int connectcosts1[4] = { 1,  0,  1, 999 };

struct distance_table 
{
	int costs[4][4];
} dt1;


/* students to write the following two routines, and maybe some others */


struct rtpkt pacote;
rtinit1() 
{
	//faz a tabela do pacote iniciar com infinito
	pacote.mincost[0] = INFINITO;
	pacote.mincost[1] = INFINITO;
	pacote.mincost[2] = INFINITO;
	pacote.mincost[3] = INFINITO;


	printf("=>Executando rinit1()\n");

	int i,j;

	for(i=0;i<NODES;i++)
	{
		for(j=0; j<NODES; j++)
		{
			dt1.costs[i][j]= INFINITO;//inicia com infinito

		}
	}

	//agora atualiza a distancia conforme a imagem
	dt1.costs[0][0]=1;
	dt1.costs[2][2]=1;

	//atualiza a tabela do pacote com a distancia minima conhecida entre os nos
	for(i=0;i<NODES;i++)
	{
		for(j=0; j<NODES; j++)
		{
			if(pacote.mincost[i] > dt1.costs[i][j])
				pacote.mincost[i] = dt1.costs[i][j];
		}
	}
	//chama tolayer2(pacote) para cada no vizinho, repassando os dados da tabela
	pacote.sourceid = 1;//esse eh o no 1
	pacote.destid = 0; //vizinho
	tolayer2(pacote);  

	pacote.sourceid = 1;
	pacote.destid = 2;
	tolayer2(pacote);  

	tolayer2(pacote);  

	printdt1(&dt1);
	printf("=>Finalizou rtinit1()\n");
}


rtupdate1(rcvdpkt)
	struct rtpkt *rcvdpkt;

{
	//faz a tabela do pacote iniciar com infinito
        pacote.mincost[0] = INFINITO;
	pacote.mincost[1] = INFINITO;
	pacote.mincost[2] = INFINITO;
	pacote.mincost[3] = INFINITO;
	
	printf("=>Executando rtupdate1()\n");

	extern float clocktime; //no documento falava para usar o extern
	printf("=>rtupdate1() %f\n recebeu do no: %d\n", clocktime, rcvdpkt->sourceid);
	printdt1(&dt1);//mostra tabela

	
	int flag = 0;
	int j,i;
	for(i = 0; i < NODES; ++i)
	{
	       // usa flag para marcar q teve alteracao
	       // teste se o custo alterou
		if(dt1.costs[i][rcvdpkt->sourceid] > rcvdpkt->mincost[i] +  dt1.costs[rcvdpkt->sourceid][rcvdpkt->sourceid])
		{
			dt1.costs[i][rcvdpkt->sourceid] = rcvdpkt->mincost[i]  + dt1.costs[rcvdpkt->sourceid][rcvdpkt->sourceid]; 
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
				if(pacote.mincost[i]  > dt1.costs[i][j]) 
					pacote.mincost[i] = dt1.costs[i][j];
			}
		}

		//chama tolayer2(pacote) para cada no vizinho, repassando os dados da tabela
		pacote.sourceid = 0;
		pacote.destid = 1;
		tolayer2(pacote);

		pacote.sourceid = 0;
		pacote.destid = 2;
		tolayer2(pacote);
		
		printf("=>Teve alteracao na tabela!\n");
		//mostra a nova tabela
		printdt1(&dt1);
	}

}


printdt1(dtptr)
	struct distance_table *dtptr;

{
	printf("             via   \n");
	printf("   D1 |    0     2 \n");
	printf("  ----|-----------\n");
	printf("     0|  %3d   %3d\n",dtptr->costs[0][0], dtptr->costs[0][2]);
	printf("dest 2|  %3d   %3d\n",dtptr->costs[2][0], dtptr->costs[2][2]);
	printf("     3|  %3d   %3d\n",dtptr->costs[3][0], dtptr->costs[3][2]);

}



linkhandler1(linkid, newcost)   
	int linkid, newcost;   
	/* called when cost from 1 to linkid changes from current value to newcost*/
	/* You can leave this routine empty if you're an undergrad. If you want */
	/* to use this routine, you'll need to change the value of the LINKCHANGE */
	/* constant definition in prog3.c from 0 to 1 */

{
}


