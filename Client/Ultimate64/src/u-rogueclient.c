/*****************************************************************
Rogue Test Client for Ultimate 64
******************************************************************/

#include <stdio.h>
#include <string.h>
#include <conio.h>
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

// Define special memory areas
#define SCREEN_RAM   ((unsigned char*)0x4800)
#define COMMS_COLOR  0x03E7

// Screen offsets
#define CELL_CHAR    0x006F
#define LEFT_CHAR    0x00BF
#define RIGHT_CHAR   LEFT_CHAR  + 40
#define HEALTH_CHARS RIGHT_CHAR + 80

//CELL_COLOR = COLOR_BASE + $006F
//LEFT_COLOR = COLOR_BASE + $00BF
//RIGHT_COLOR = LEFT_COLOR + 40

// Data Types
typedef unsigned char byte;

// Prototypes to call assembly routines
void fastcall screen_init();
void fastcall color_lookup();
void fastcall sound_init();
void fastcall sound_play(byte fx);

// Global Variables
byte socketnr = 0;
byte send_buffer[17] = { 1, 1, 65, 66, 67, 6,7,8,9,10,11, 12, 13, 14,15,16,17 };
byte soundcounter = 0;

void clear_screen()
{
	printf("%c", 147);
}

void send_announce()
{
	// TODO
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
	color_lookup();

	// Messages
	memcpy(SCREEN_RAM + 800, uii_data + 358, 160);

	// Current Cell
	memcpy(SCREEN_RAM + CELL_CHAR, uii_data + 518, 1);
	
	/* stx CELL_CHAR
		lda colortable, x
		sta CELL_COLOR*/

	// Held - Left
	memcpy(SCREEN_RAM + LEFT_CHAR, uii_data + 519, 1);

	// Held - Right
	memcpy(SCREEN_RAM + RIGHT_CHAR, uii_data + 520, 1);
	
	// Health
	memcpy(SCREEN_RAM + HEALTH_CHARS, uii_data + 521, 3);

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
			return;
	}
}

void network_init()
{
	int count = 0;
	//char *host = "192.168.7.51";
	//char *host = "rogue.jammingsignal.com";
	char *host = "192.168.7.14";

	printf("Rogue U64 Test\n");

	if (uii_isdataavailable())
	{
		printf("\naborting a previous command...");
		uii_abort();
	}

	uii_identify();
	printf("\n\nIdentify: %s\nStatus: %s", uii_data, uii_status);

	// -----------------------------------------------------------
	// Network interface target
	// -----------------------------------------------------------

	uii_getinterfacecount();

	uii_getipaddress();
	printf("\n\nIP Address: %d.%d.%d.%d", uii_data[0], uii_data[1], uii_data[2], uii_data[3]);
	printf("\n   Netmask: %d.%d.%d.%d", uii_data[4], uii_data[5], uii_data[6], uii_data[7]);
	printf("\n   Gateway: %d.%d.%d.%d", uii_data[8], uii_data[9], uii_data[10], uii_data[11]);

	printf("\n\nConnecting to: %s", host);
	socketnr = uii_tcpconnect(host, 3008);
	printf("\n    Status: %s  (Socket #%d)", uii_status, socketnr);

	// TODO, check status
}

void player_login()
{
	printf("\n\nWriting announce...\n");
	uii_tcpsocketwrite(socketnr, send_buffer);
	printf("\n    Status: %s", uii_status);
}

void game_loop()
{
	int received = 0;

	screen_init();
	sound_init();
	POKE(0x028A, 0xFF); //  All keys repeat

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
	POKEW(0xD020, 0); // Back screen
	clear_screen();
	network_init();

	player_login();
	game_loop();
}