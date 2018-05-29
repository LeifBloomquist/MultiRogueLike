// rogue.js

   var imgfont = document.createElement("img");
   imgfont.src = "roguefont.png";
   
   var playerstext = document.getElementById("players");

   var screen = document.getElementById("screen");                       
   var context = screen.getContext("2d");
   
   var person = "name";
   
   context.imageSmoothingEnabled = false;
   context.mozImageSmoothingEnabled = false;
   context.webkitImageSmoothingEnabled = false;
   context.msImageSmoothingEnabled = false;
   
   var audio_counter = -1;
   var audio_step    = new Audio('sfx/step.mp3');
   var audio_blocked = new Audio('sfx/blocked.mp3');
   var audio_attack  = new Audio('sfx/attack.mp3');
   
   var ws = null;

   function WebSocketStart()
   {
      if ("WebSocket" in window)
      {
         person = prompt("Please enter your name", "");

         if (person == null || person == "")  // Cancelled 
         {
            return;
         }     
         
         person = person.substring(0, 16);
          
         // Let us open a web socket
         // ws = new WebSocket("ws://localhost:3007/Rogue");         
         ws = new WebSocket("ws://rogue.jammingsignal.com:3007/Rogue");
         ws.binaryType = 'arraybuffer';
	
         // Web Socket is connected
         ws.onopen = function()
         {          
             sendAnnounce(person);
         };
	
         ws.onmessage = function(evt) 
         { 
            var byteArray = new Uint8Array(evt.data);
            drawScreen(byteArray.slice(1,358));
            
            drawString(person.toUpperCase(),24,1);
            drawString("I SEE:",24,3);
            drawString("LEFT: ",24,5);
            drawString("RIGHT:",24,6);             
            drawString("HEALTH:",24,8);
            
             for (var p = 0; p < 40; p++) 
             {
                drawChar(byteArray[358+p], p, 20);
                drawChar(byteArray[398+p], p, 21);
                drawChar(byteArray[438+p], p, 22);
                drawChar(byteArray[478+p], p, 23);
             }
            
            drawChar(byteArray[518], 31, 3); // Seen
            drawChar(byteArray[519], 31, 5); // Left
            drawChar(byteArray[520], 31, 6); // Right
            drawChar(byteArray[521], 31, 8); // Health
            drawChar(byteArray[522], 32, 8); // Health
            drawChar(byteArray[523], 33, 8); // Health
            
            // Sound effects
            if (audio_counter != byteArray[524])
            {
               playSound(byteArray[525]);
            }
            audio_counter = byteArray[524];
                 
            playerstext.innerHTML = "Number of players in dungeon: " + byteArray[526];
                 
            // TODO, XP, Gold, etc.
         };
	
         ws.onclose = function()
         { 
            // websocket is closed.
            alert("Server connection has been closed.");
            window.location.replace('index.html'); 
         };
		
         window.onbeforeunload = function(event) 
         {
            ws.close();
         };
      }
      
      else
      {
         // The browser doesn't support WebSocket
         alert("WebSocket NOT supported by your Browser!");
      }
   }
   
   function sendAnnounce(name)
   {      
      if (ws != null)
      {
         ws.send("1" + name);
      }  
   }
   
   var command_counter = 0;

   function sendCommand(key)
   {
      var command = new Uint8Array(3);
      command[0] = 2;   // Client Update  
      command[1] = command_counter++;   // Counter
      command[2] = key;
      
      if (ws != null)
      {
         ws.send(command);
      }  
   }

   // Handle most keys
   document.onkeypress = function(event) 
   { 
      var key = event.which;
      sendCommand(key); 
   };   
  
   // Handle arrow keys
   document.onkeydown = function(event) 
   { 
      var key = event.which;
        
      switch (key) 
      { 
        case 37: 
           moveWest(); 
           break; 

        case 38:
           moveNorth();
           break; 

        case 39:
           moveEast();
           break; 

        case 40:
           moveSouth();
           break;
     } 
  };     

  function Use(hand)
  {
      switch (hand) 
      {
        case 0:         
          sendCommand(42); // *
          break;
          
        case 1:         
          sendCommand(44); // ,
          break;
          
        case 2:         
          sendCommand(46); // .
          break;
      }          
  }
  
  function Examine(hand)
  {
      switch (hand) 
      {
        case 0:         
          sendCommand(105);  // i
          break;
      }          
  }
  
  function Help()
  {
     var helptext = "Rogue Multiplayer Help (Keys)\n\n";
     
     helptext += "QWE\n";
     helptext += "ASD = Move  (or use Arrow Keys)\n";
     helptext += "ZXC\n\n";
     helptext += "SHIFT+Move = Attack\n\n";
     helptext += "J = Pick up item (Left  Hand)\n";
     helptext += "K = Pick up item (Right Hand)\n\n";
     helptext += "SHIFT+J,K = Drop Item (Left, Right)\n\n";
     helptext += "U = Use item at current location\r\n";
     helptext += ", = Use item (Left)\n";
     helptext += ". = Use item (Right)\n\n";
     helptext += "I = Inspect item at current location\n\n";
     helptext += "H = Shows C64 Help Screen\n";
     
     alert(helptext);
  }
  

  function moveNorth()
  {
     sendCommand(119);
  }

  function moveSouth()
  {
     sendCommand(115);
  }

  function moveWest()
  {
     sendCommand(97);
  }

  function moveEast()
  {
     sendCommand(100);
  }

  function myScale()
  {
     context.setTransform(1, 0, 0, 1, 0, 0);
     var scale = 2;  // ddscale.value;
     context.scale(scale,scale);
     repaint();
  }

  function repaint()
  {
    // Background (Not visible)
    context.fillStyle = "yellow"; 
    context.fillRect(0, 0, screen.width, screen.height);

    // Screen
    context.fillStyle = "black";
    context.fillRect(0, 0, 320, 200);
    
    // Box
    context.fillStyle = "grey";
    context.fillRect(0, 0, 184, 152);   
  }

  function load()
  {
      myScale();
      WebSocketStart();
  }

  function drawScreen(charArray)
  {
    var char = 0;

    for (row = 0; row < 17; row++) 
    {
       for (col=0; col < 21; col++)
       {
          drawChar(charArray[char], col+1, row+1);  // +1 to make room for border            
          char++;
       }
    }
  }

  function drawChar(num, x, y)
  {
     const FONTWIDTH = 32;
     const CHARSIZE  = 8;
     
     var col = num % FONTWIDTH;
     var row = Math.floor(num/FONTWIDTH);
     
     col *= CHARSIZE;
     row *= CHARSIZE;

     x *= CHARSIZE;
     y *= CHARSIZE;
   
     context.drawImage(imgfont, col, row, CHARSIZE, CHARSIZE, x, y, CHARSIZE, CHARSIZE); 
  }
  
  function drawString(text, x, y)
  {
     for (var p = 0; p < text.length; p++) 
     {
        var l = text.charCodeAt(p);
        drawChar(l,x+p,y);
     }
  }
  
  function playSound(sound)
  {
     switch(sound) 
     {
        case 0:
           return;
  
        case 1:
           audio_step.currentTime = 0;
           audio_step.play();
           return;
        
        case 2:
           audio_blocked.currentTime = 0;
           audio_blocked.play();
           return;
           
        case 3:
        case 5:
           audio_attack.currentTime = 0;
           audio_attack.play();
           return;
      }
  }
  
  
  
  imgfont.addEventListener("load", load());

// EOF