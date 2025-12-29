/*****************************************************************
Ultimate 64/II+ Command Library
Scott Hutter, Francesco Sblendorio

Based on ultimate_dos-1.2.docx and command interface.docx
https://github.com/markusC64/1541ultimate2/tree/master/doc

Network Interface only version by Leif Bloomquist

******************************************************************/
#include <string.h>
#include <peekpoke.h>
#include "ultimate_lib_net.h"

static unsigned char *cmddatareg = (unsigned char *)CMD_DATA_REG;
static unsigned char *controlreg = (unsigned char *)CONTROL_REG;
static unsigned char *statusreg = (unsigned char *)STATUS_REG;
//static unsigned char *idreg = (unsigned char *)ID_REG;
static unsigned char *respdatareg = (unsigned char *)RESP_DATA_REG;
static unsigned char *statusdatareg = (unsigned char *)STATUS_DATA_REG;

unsigned char uii_data[DATA_QUEUE_SZ * 2];
unsigned char uii_status[STATUS_QUEUE_SZ];
unsigned char temp_string_onechar[2];
int uii_data_index;
int uii_data_len;

unsigned char uii_target = TARGET_NETWORK;

unsigned char read_cmd[] = { 0x00,NET_CMD_TCP_SOCKET_READ, 0x00, 0xF4, 0x01 };  // Length = 500 bytes

void uii_logtext(char *text)
{
#ifdef DEBUG
	printf("%s", text);
#else
	text = 0;  // to eliminate the warning in cc65
#endif
}

void uii_logstatusreg(void)
{
#ifdef DEBUG
	printf("\nstatus reg %p = %d",statusreg, *statusreg);
#endif
}

void uii_settarget(unsigned char id)
{
	uii_target = id;
}

void uii_identify(void)
{
	unsigned char cmd[] = {0x00,DOS_CMD_IDENTIFY};
	uii_settarget(TARGET_DOS1);
	uii_sendcommand(cmd, 2);
	uii_readdata();
	uii_readstatus();
	uii_accept();
}

void uii_echo(void)
{
	unsigned char cmd[] = {0x00,DOS_CMD_ECHO};
	uii_settarget(TARGET_DOS1);
	uii_sendcommand(cmd, 2);

	uii_readdata();
	uii_readstatus();
	uii_accept();
}

void uii_sendcommand(unsigned char *bytes, int count)
{
	int x =0;
	int success = 0;
	
	bytes[0] = uii_target;
	
	while(success == 0)
	{
		// Wait for idle state
		//uii_logtext("\nwaiting for cmd_busy to clear...");
		//uii_logstatusreg();
		
		while ( !(((*statusreg & 32) == 0) && ((*statusreg & 16) == 0)))  {
	//		uii_logtext("\nwaiting...");
	//		uii_logstatusreg();
		};
		
		// Write byte by byte to data register
//		uii_logtext("\nwriting command...");
		while(x<count)
			*cmddatareg = bytes[x++];
		
		// Send PUSH_CMD
//		uii_logtext("\npushing command...");
		*controlreg |= 0x01;
		
//		uii_logstatusreg();
		
		// check ERROR bit.  If set, clear it via ctrl reg, and try again
		if ((*statusreg & 4) == 4)
		{
			//uii_logtext("\nerror was set. trying again");
			*controlreg |= 0x08;
		}
		else
		{
			//uii_logstatusreg();
			
			// check for cmd busy
			while ( ((*statusreg & 32) == 0) && ((*statusreg & 16) == 16) )
			{
	//			uii_logtext("\nstate is busy");
			}
			success = 1;
		}
	}
	
	//uii_logstatusreg();
	//uii_logtext("\ncommand sent");
	
}

void uii_accept(void)
{
	// Acknowledge the data
	*controlreg |= 0x02;
    
    // Wait for ACK to the ACK...New for U64 Firmware 1.37
    while ( !(*statusreg & 2) == 0 )  
    {
        ;
	};
}

int uii_isdataavailable(void)
{
	if ( ((*statusreg & 128) == 128 ) )
		return 1;
	else
		return 0;
}

int uii_isstatusdataavailable(void)
{
	if ( ((*statusreg & 64) == 64 ) )
		return 1;
	else
		return 0;
}

void uii_abort(void)
{
	// abort the command
	uii_logstatusreg();
	uii_logtext("\nsending abort");
	*controlreg |= 0x04;
}

unsigned int uii_readdata(void) 
{
	unsigned int count = 0;
	uii_data[0] = 0;

	// If there is data to read
	//while (uii_isdataavailable())
	while ((*statusreg & 128) == 128)
	{
		uii_data[count++] = *respdatareg;
		//count++;		
	}

	uii_data[count] = 0;
	return count;
}

int uii_readstatus(void) 
{
	int count = 0;
	
	uii_status[0] = 0;
	
	//uii_logtext("\n\nreading status...");
	//uii_logstatusreg();

	while(uii_isstatusdataavailable())
	{
		uii_status[count++] = *statusdatareg;
	}
	
	uii_status[count] = 0;
	return count;
}

void uii_getinterfacecount(void)
{
	unsigned char tempTarget = uii_target;
	unsigned char cmd[] = {0x00,NET_CMD_GET_INTERFACE_COUNT};
	
	uii_settarget(TARGET_NETWORK);
	uii_sendcommand(cmd, 0x02);

	uii_readdata();
	uii_readstatus();
	uii_accept();
	
	uii_target = tempTarget;
}

void uii_getipaddress(void)
{
	unsigned char tempTarget = uii_target;
	unsigned char cmd[] = {0x00,NET_CMD_GET_IP_ADDRESS, 0x00}; // interface 0 (theres only one)
	
	uii_settarget(TARGET_NETWORK);
	uii_sendcommand(cmd, 0x03);

	uii_readdata();
	uii_readstatus();
	uii_accept();
	
	uii_target = tempTarget;
}

unsigned char uii_tcpconnect(char* host, unsigned short port)
{
	unsigned char tempTarget = uii_target;
	unsigned char cmd[] = {0x00,NET_CMD_TCP_SOCKET_CONNECT, 0x00, 0x00};
	int x=0;
	unsigned char* fullcmd;
	
	fullcmd = (unsigned char *)malloc(4 + strlen(host)+ 1);
	fullcmd[0] = cmd[0];
	fullcmd[1] = cmd[1];
	fullcmd[2] = port & 0xff;
	fullcmd[3] = (port>>8) & 0xff;
	
	for(x=0;x<strlen(host);x++)
		fullcmd[x+4] = host[x];
	
	fullcmd[4+strlen(host)] = 0x00;
	
	uii_settarget(TARGET_NETWORK);
	uii_sendcommand(fullcmd, 4+strlen(host)+1);

	free(fullcmd);

	uii_readdata();
	uii_readstatus();
	uii_accept();
	
	uii_target = tempTarget;

	uii_data_index = 0;
	uii_data_len = 0;
	return uii_data[0];
}

void uii_tcpclose(unsigned char socketid)
{
	unsigned char tempTarget = uii_target;
	unsigned char cmd[] = {0x00,NET_CMD_TCP_SOCKET_CLOSE, 0x00};
	cmd[2] = socketid;
	
	uii_settarget(TARGET_NETWORK);
	uii_sendcommand(cmd, 0x03);

	uii_readdata();
	uii_readstatus();
	uii_accept();
	
	uii_target = tempTarget;
}

int uii_tcpsocketread(unsigned char socketid, unsigned short length)
{
	unsigned char tempTarget = uii_target;
	unsigned char cmd[] = {0x00,NET_CMD_TCP_SOCKET_READ, 0x00, 0x00, 0x00};

	cmd[0] = cmd[0];
	cmd[1] = cmd[1];
	cmd[2] = socketid;
	cmd[3] = length & 0xff;
	cmd[4] = (length>>8) & 0xff;
	
	uii_settarget(TARGET_NETWORK);
	uii_sendcommand(cmd, 0x05);

	uii_readdata();
	uii_readstatus();
	uii_accept();
	
	uii_target = tempTarget;
	return uii_data[0] | (uii_data[1]<<8);
}

void uii_tcpsocketread_opt_init(unsigned char socketid)
{
	read_cmd[0] = TARGET_NETWORK;  // bytes[0] = uii_target;
	read_cmd[2] = socketid;
	uii_settarget(TARGET_NETWORK);
}

unsigned int uii_tcpsocketread_opt()
{
	unsigned int count = 0;
	
	// Replacing uii_sendcommand(read_cmd, 0x05);   -------------

	while (1)
	{
		// Wait for idle state
		while (!(((*statusreg & 32) == 0) && ((*statusreg & 16) == 0))) 
		{
			;
		}

		// Write byte by byte to data register (unrolled)
		*cmddatareg = read_cmd[0];		 //  while (x < 5)
		*cmddatareg = read_cmd[1];		 //  {
		*cmddatareg = read_cmd[2];		 //  	*cmddatareg = read_cmd[x++];
		*cmddatareg = read_cmd[3];		 //  }
		*cmddatareg = read_cmd[4];		 // 

		// Send PUSH_CMD
		*controlreg |= 0x01;

		// check ERROR bit.  If set, clear it via ctrl reg, and try again
		if (*statusreg & 4) //((*statusreg & 4) == 4)
		{
			*controlreg |= 0x08;
		}
		else
		{
			// check for cmd busy
			while (((*statusreg & 32) == 0) && (*statusreg & 16)) //while (((*statusreg & 32) == 0) && ((*statusreg & 16) == 16))
			{
				;
			}
			break;
		}
	}

	// End send-command -----------------------

	// If there is data to read
	while (*statusreg & 128) 
	{
		uii_data[count++] = *respdatareg;	
	}

	// Acknowledge the data
	uii_accept();

	return count - 2;
}

int uii_tcplistenstart(unsigned short port)
{
	unsigned char tempTarget = uii_target;
	unsigned char cmd[] = {0x00,NET_CMD_TCP_LISTENER_START, 0x00, 0x00};
	cmd[2] = port & 0xff;
	cmd[3] = (port>>8) & 0xff;
	
	uii_settarget(TARGET_NETWORK);
	uii_sendcommand(cmd, 0x04);

	uii_readdata();
	uii_readstatus();
	uii_accept();
	
	uii_target = tempTarget;
	return uii_data[0] | (uii_data[1]<<8);
}

int uii_tcplistenstop()
{
	unsigned char tempTarget = uii_target;
	unsigned char cmd[] = {0x00,NET_CMD_TCP_LISTENER_STOP};
	
	uii_settarget(TARGET_NETWORK);
	uii_sendcommand(cmd, 0x02);

	uii_readdata();
	uii_readstatus();
	uii_accept();
	
	uii_target = tempTarget;
	return uii_data[0] | (uii_data[1]<<8);
}

int uii_tcpgetlistenstate()
{
	unsigned char tempTarget = uii_target;
	unsigned char cmd[] = {0x00,NET_CMD_GET_LISTENER_STATE};
	
	uii_settarget(TARGET_NETWORK);
	uii_sendcommand(cmd, 0x02);

	uii_readdata();
	uii_readstatus();
	uii_accept();
	
	uii_target = tempTarget;
	return uii_data[0] | (uii_data[1]<<8);
}

unsigned char uii_tcpgetlistensocket()
{
	unsigned char tempTarget = uii_target;
	unsigned char cmd[] = {0x00,NET_CMD_GET_LISTENER_SOCKET};
	
	uii_settarget(TARGET_NETWORK);
	uii_sendcommand(cmd, 0x02);

	uii_readdata();
	uii_readstatus();
	uii_accept();
	
	uii_target = tempTarget;
	return uii_data[0] | (uii_data[1]<<8);
}

void uii_tcpsocketwrite_convert_parameter(unsigned char socketid, char *data, int ascii)
{
	unsigned char tempTarget = uii_target;
	unsigned char cmd[] = {0x00,NET_CMD_TCP_SOCKET_WRITE, 0x00};
	int x=0;
	unsigned char* fullcmd;
	char c;
	
	fullcmd = (unsigned char *)malloc(3 + strlen(data));
	fullcmd[0] = cmd[0];
	fullcmd[1] = cmd[1];
	fullcmd[2] = socketid;
	
	for(x=0;x<strlen(data);x++){
		c = data[x];
		if (ascii) {
			if ((c>=97 && c<=122) || (c>=193 && c<=218)) c &= 95;
            else if (c>=65 && c<=90) c |= 32;
            else if (c==13) c=10;
		}
		fullcmd[x+3] = c;
	}
	
	fullcmd[3+strlen(data)+1] = 0;
	
	uii_settarget(TARGET_NETWORK);
	uii_sendcommand(fullcmd, 3+strlen(data));

	free(fullcmd);

	uii_readdata();
	uii_readstatus();
	uii_accept();

	uii_target = tempTarget;
	
	uii_data_index = 0;
	uii_data_len = 0;
}

void uii_tcpsocketwritechar(unsigned char socketid, char one_char) {
	temp_string_onechar[0] = one_char;
	temp_string_onechar[1] = 0;

	uii_tcpsocketwrite(socketid, (char*)temp_string_onechar);
}

void uii_tcpsocketwrite(unsigned char socketid, char *data) {
	uii_tcpsocketwrite_convert_parameter(socketid, data, 0);
}

void uii_tcpsocketwrite_ascii(unsigned char socketid, char *data) {
	uii_tcpsocketwrite_convert_parameter(socketid, data, 1);
}

char uii_tcp_nextchar(unsigned char socketid) {
    char result;
    if (uii_data_index < uii_data_len) {
        result = uii_data[uii_data_index+2];
        uii_data_index++;
    } else {
        do {
            uii_data_len = uii_tcpsocketread(socketid, DATA_QUEUE_SZ-4);
            if (uii_data_len == 0) return 0; // EOF
        } while (uii_data_len == -1);
        result = uii_data[2];
        uii_data_index = 1;
    }
    return result;
}

int uii_tcp_nextline_convert_parameter(unsigned char socketid, char *result, int swapCase) {
    int c, count = 0;
    *result = 0;
    while ((c = uii_tcp_nextchar(socketid)) != 0 && c != 0x0A) {
    	if (c == 0x0D){
    		continue;
    	} else if (swapCase) {
            if ((c>=97 && c<=122) || (c>=193 && c<=218)) c &= 95;
            else if (c>=65 && c<=90) c |= 32;
        }
        result[count++] = c;
    }
    result[count] = 0;
    return c != 0 || count > 0;
}

int uii_tcp_nextline(unsigned char socketid, char *result) {
	return uii_tcp_nextline_convert_parameter(socketid, result, 0);
}

int uii_tcp_nextline_ascii(unsigned char socketid, char *result) {
	return uii_tcp_nextline_convert_parameter(socketid, result, 1);
}

void uii_reset_uiidata() {
	uii_data_len = 0;
	uii_data_index = 0;
	memset(uii_data, 0, DATA_QUEUE_SZ*2);
	memset(uii_status, 0, STATUS_QUEUE_SZ);
}

void uii_tcp_emptybuffer() {
	uii_data_index = 0;
}
