/*****************************************************************
Rogue Test Client for Ultimate 64
******************************************************************/

#include <stdio.h>
#include <string.h>
#include <conio.h>
#include <ctype.h>
#include <c64.h>
#include <peekpoke.h>
#include "lib/ultimate_lib_net.h"

// Defines
// Packet Types - C64 to Server
#define PACKET_ANNOUNCE 1
#define PACKET_CLIENT_UPDATE 2

// Packet Types - Server to C64
#define PACKET_ANNOUNCE_REPLY 128
#define PACKET_SERVER_UPDATE  129
#define PACKET_SERVER_UPDATE_LENGTH 528

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
#define CG_RVS 18   // revs - on
#define CG_NRM 146  // revs - off
#define CG_DEL 20   // Delete
#define CG_CLR 147   // Delete


// Define special memory areas
#define SCREEN_RAM   ((unsigned char*)0x4800)
#define COMMS_COLOR  0x03E7

// Screen offsets
#define CELL_CHAR    0x006F
#define LEFT_CHAR    0x00BF
#define RIGHT_CHAR   LEFT_CHAR  + 40
#define HEALTH_CHARS RIGHT_CHAR + 80

// Data Types
typedef unsigned char byte;

// Prototypes to call assembly routines
void fastcall screen_init();
void fastcall color_lookup();
void fastcall sound_init();
void fastcall sound_play(byte fx);

// Global Variables
byte socketnr = 0;
byte soundcounter = 0;

// Data buffers
byte send_buffer[2] = { 0, 0 };

void clear_screen()
{
	printf("%c", CG_CLR);
}

void delete()
{
	printf("%c", CG_DEL);
}

void color(byte color)
{
	printf("%c", color);
}

int text_input(char *text, byte max)
{
	char c;
	byte i = 0;

	cursor(1);

	while (1) 
	{
		c = cgetc();

		if (isprint(c) || isdigit(c))  // is a printable char or number 
		{
			text[i] = c;
			cputc(c);
			++i;
		}

		if (c == CG_DEL)  // user pressed backspace 
		{
			if (i > 0)
			{
				--i;

				text[i] = '\0';
				delete();
			}
		}

		if (c == '\n')  // user pressed return
		{
			if (i != 0)
			{
				text[i] = '\0';
				cursor(0);
				return i;
			}
		}

		if (i == max) continue;  // maxed out	
	}
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
	//printf("\n%cDEBUG: [%s] [%s] [%d]\n", CG_PUR, uii_status, temp, status);
	return status;
}

void send_announce(char *name)
{
	int i = 0;

	byte announce_buffer[17] = { PACKET_ANNOUNCE, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
	strncpy(announce_buffer+2, name, 15);

	// Workaround for U64 - null bytes terminate the packet.  Replace with $FF and strip out again on the server
	for (i = 0; i < 17; i++)
	{
		if (announce_buffer[i] == 0) announce_buffer[i] = 255;
	}

	uii_tcpsocketwrite(socketnr, announce_buffer);
}

void send_action(char key)
{
	send_buffer[0] = key;
	send_buffer[1] = 0;
	uii_tcpsocketwrite(socketnr, send_buffer);
}

void handle_server_update(byte *uii_data)
{
	byte temp_soundcounter = 0;
	byte soundeffect = 0;

	COLOR_RAM[COMMS_COLOR]++;

	// Screen (17 Rows)
	memcpy(SCREEN_RAM +  41, uii_data +   1, 21);
	memcpy(SCREEN_RAM +  81, uii_data +  22, 21);
	memcpy(SCREEN_RAM + 121, uii_data +  43, 21);
	memcpy(SCREEN_RAM + 161, uii_data +  64, 21);
	memcpy(SCREEN_RAM + 201, uii_data +  85, 21);
	memcpy(SCREEN_RAM + 241, uii_data + 106, 21);
	memcpy(SCREEN_RAM + 281, uii_data + 127, 21);
	memcpy(SCREEN_RAM + 321, uii_data + 148, 21);
	memcpy(SCREEN_RAM + 361, uii_data + 169, 21);
	memcpy(SCREEN_RAM + 401, uii_data + 190, 21);
	memcpy(SCREEN_RAM + 441, uii_data + 211, 21);
	memcpy(SCREEN_RAM + 481, uii_data + 232, 21);
	memcpy(SCREEN_RAM + 521, uii_data + 253, 21);
	memcpy(SCREEN_RAM + 561, uii_data + 274, 21);
	memcpy(SCREEN_RAM + 601, uii_data + 295, 21);
	memcpy(SCREEN_RAM + 641, uii_data + 316, 21);
	memcpy(SCREEN_RAM + 681, uii_data + 337, 21);

	// Messages
	memcpy(SCREEN_RAM + 800, uii_data + 358, 160);

	memcpy(SCREEN_RAM + CELL_CHAR, uii_data + 518, 1);     // Current Cell
	memcpy(SCREEN_RAM + LEFT_CHAR, uii_data + 519, 1);     // Held - Left
	memcpy(SCREEN_RAM + RIGHT_CHAR, uii_data + 520, 1);    // Held - Right
	memcpy(SCREEN_RAM + HEALTH_CHARS, uii_data + 521, 3);  // Health

	// Colorize the screen
	color_lookup();

	// Sound effects
	temp_soundcounter = uii_data[524];

	if (temp_soundcounter != soundcounter)  // Sound changed
	{
		soundcounter = temp_soundcounter;
		soundeffect = uii_data[525];

		sound_play(soundeffect);
	}

	// TODO, number of players
}

void handle_packet(byte *uii_data)
{
	switch (uii_data[2])   // 0 and 1 are length
	{
		case PACKET_SERVER_UPDATE:
			handle_server_update(uii_data+2);
			break;

		default:     // No other types handled yet (possibly corrupted packet)
			asm("inc $d020"); // DEBUG !!!!
			return;
	}
}

void network_init()
{
	int status = 0;

	//char *host = "192.168.7.51";
	//char *host = "rogue.jammingsignal.com";
	char *host = "192.168.7.14";

	clear_screen();
	color(CG_YEL);
	printf("Rogue Ultimate 64 Test Client\n\n");

	color(CG_GR2);
	printf("Build:%c %s %s\n\n", CG_BLU, __DATE__, __TIME__);

	if (uii_isdataavailable())
	{
		color(CG_RED);
		printf("Aborting a previous command...\n");
		uii_abort();
	}

	color(CG_GR2);
	printf("Identifying...");
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
	socketnr = uii_tcpconnect(host, 3008);

	status = get_uii_status();
	if (status == 0)
	{
		color(CG_LGN);
		printf("\nConnected!  Press any key to log in.");
		cgetc();
		return;
	}
	else
	{
		color(CG_RED);
		printf("\nStatus: %s\n\n", uii_status);
		printf("*** Failed to Connect to Server ***\n");
		while (1);
	}
}

void player_login()
{
	int status = 0;
	char name[20];

	clear_screen();
	color(CG_RED);
	printf("Rogue Version 0.005 for Ultimate 64\n\n");	
	printf("%cConcept+Game Code: %cLeif Bloomquist\n\n", CG_LBL, CG_WHT);
	printf("%cNetworking Code:   %cScott Hutter\n\n", CG_LBL, CG_WHT);
	printf("%cContributors:      %cRobin Harbron\n", CG_LBL, CG_WHT);
	printf("                   groepaz\n");
	printf("                   qzerow\n\n");
	printf("%cControls:          %cJoystick in Port 2\n", CG_LBL, CG_YEL);
	printf("                   Press F1 for Keys\n\n");
	
	color(CG_LGN);
	printf("Login: ");

	color(CG_WHT);
	text_input(name, 15);
	send_announce(name);

	status = get_uii_status();
	if (status == 0)
	{		
		return;
	}
	else
	{
		color(CG_RED);
		printf("\nStatus: %s\n\n", uii_status);
		printf("*** Failed to log in ***\n");
		cgetc();
	}
}

void game_loop()
{
	int received = 0;

	screen_init();
	sound_init();

	POKE(0x028A, 0xFF); //  All keys repeat/  Also $028B	651		Speed of key - repeat?	

	// Main Game loop
	while (1)
	{
		// Server Updates
		received = uii_tcpsocketread(socketnr, PACKET_SERVER_UPDATE_LENGTH);

		if (received == PACKET_SERVER_UPDATE_LENGTH)
		{
			handle_packet(uii_data);
		}

		// Player Commands (keyboard)
		if (kbhit())
		{
			send_action(cgetc());
		}
	}

	printf("\n\nClosing connection");
	uii_tcpclose(socketnr);
	printf("\n    Status: %s", uii_status);
}


void main(void)
{
	POKEW(0xD020, 0);   // Black screen
	POKE(0x0291, 128);  // Disable Shift-C=

	network_init();
	player_login();
	game_loop();
}