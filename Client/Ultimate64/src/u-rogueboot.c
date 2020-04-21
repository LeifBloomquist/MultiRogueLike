/*****************************************************************
Rogue Boot Client for Ultimate 64
******************************************************************/

#include <stdio.h>
#include <string.h>
#include <conio.h>
#include <ctype.h>
#include <c64.h>
#include <peekpoke.h>
#include "lib/ultimate_lib_net.h"

#define BORDER 0xD020
#define SHIFT  0x028D
#define START  0x6000

#define CG_BLK 144
#define CG_WHT 5
#define CG_RED 28
#define CG_CYN 159
#define CG_PUR 156
#define CG_GRN 30
#define CG_BLU 31
#define CG_YEL 158
#define CG_BRN 149
#define CG_ORG 129
#define CG_PNK 150
#define CG_GR1 151
#define CG_GR2 152
#define CG_LGN 153
#define CG_LBL 154
#define CG_GR3 155

// Data Types
typedef unsigned char byte;

void clear_screen()
{
	printf("%c", 147);
}

void color(byte color)
{
	printf("%c", color);
}

int get_uii_status()
{
	int status = -1;
	char* ptr = NULL;
	char temp[100];
	strcpy(temp, uii_status);

	ptr = strchr(temp, ',');
	*ptr = '\0';

	status = atoi(temp);
	return status;
}

void main(void)
{
	int count = 0;
	int status = 0;

	//char *host = "rogue.jammingsignal.com";
	char *host = "192.168.7.14";
	unsigned char socketnr = 0;
	unsigned int datacount = 0;
	unsigned int received = 0;
	unsigned int address = START;

	clear_screen();
	POKEW(BORDER, 0);
	POKE(0x0291, 128);    // Disable Shift-C=

	color(CG_YEL);
	printf("Ultimate 64 Rogue Bootloader\n");

	if (uii_isdataavailable())
	{
		color(CG_RED);
		printf("\nAborting a previous command...");
		uii_abort();
	}

	color(CG_GR2);
	printf("\nIdentifying...");
	uii_identify();

	status = get_uii_status();
	if (status == 0)
	{
		color(CG_LGN);
		printf("%s\n", uii_data);
	}
	else
	{
		color(CG_RED);
		printf("\nStatus: %s (%s)\n", uii_data, uii_status);
	}

	// -----------------------------------------------------------
	// Network interface target
	// -----------------------------------------------------------

	color(CG_GR2);
	printf("\nInitializing Network...");

	uii_getinterfacecount();
	uii_getipaddress();

	status = get_uii_status();
	if (status == 0)
	{
		color(CG_LGN);
		printf("%s\n", uii_status);
		color(CG_LBL);
	}
	else
	{
		color(CG_RED);
		printf("\nStatus: %s (%s)\n", uii_data, uii_status);
	}

	printf("\nIP Address: %d.%d.%d.%d\n", uii_data[0], uii_data[1], uii_data[2], uii_data[3]);
	printf("   Netmask: %d.%d.%d.%d\n", uii_data[4], uii_data[5], uii_data[6], uii_data[7]);
	printf("   Gateway: %d.%d.%d.%d\n", uii_data[8], uii_data[9], uii_data[10], uii_data[11]);

	color(CG_GR2);
	printf("\nConnecting to server:%c %s\n", CG_BLU, host);
	socketnr = uii_tcpconnect(host, 3064);

	status = get_uii_status();
	if (status != 0)
	{
		color(CG_RED);
		printf("\nStatus: %s\n\n", uii_status);
		printf("*** Failed to Connect to Server ***\n");
		while (1);
	}

	color(CG_GR3);
	printf("\nDownloading game data");

	while (uii_success())
	{
		received = uii_tcpsocketread(socketnr, 528);
		memcpy(address, uii_data + 2, received);
		address += received;
		datacount += received;

		printf(".");
	}

	color(CG_LGN);
	printf("\n\nReceived: %d bytes\n", datacount);
	uii_tcpclose(socketnr);

	// Debug - pause if shift is pressed
	while (PEEK(SHIFT))
	{
		;
	}

	asm("jmp %w", START);
}