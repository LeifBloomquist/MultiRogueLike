// rogue.js

   var imgfont = document.createElement("img");
   imgfont.src = "roguefont.png";
   
   var actiontext = document.getElementById("action");

   var screen = document.getElementById("screen");                       
   var context = screen.getContext("2d");
   
   context.imageSmoothingEnabled = false;
   context.mozImageSmoothingEnabled = false;
   context.webkitImageSmoothingEnabled = false;
   context.msImageSmoothingEnabled = false;
   
   var ws = null;

   function WebSocketTest()
   {
      if ("WebSocket" in window)
      {
         var person = prompt("Please enter your name", "");

         if (person == null || person == "")  // Cancelled 
         {
            return;
         }     
         
         person = person.substring(0, 16);
          
         // Let us open a web socket
         ws = new WebSocket("ws://localhost:3007/Rogue");         
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
         };
	
         ws.onclose = function()
         { 
            // websocket is closed.
            alert("Connection is closed!"); 
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
     // alert(name);
      
     // var announce = [];
    //  announce.push(1);   // Announce

    //  var utf8 = unescape(encodeURIComponent(name));

   //   var announce = [];
//      for (var i = 0; i < utf8.length; i++) 
///      {
///          announce.push(utf8.charCodeAt(i));
//      }
      
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

   document.onkeypress = function(event) 
   { 
      var key = event.which;
      sendCommand(key); 
   };   
  
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

  function Use()
  {
     sendCommand(42);
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
    // Background
    context.fillStyle = "yellow"; 
    context.fillRect(0, 0, screen.width, screen.height);

    // Screen
    context.fillStyle = "blue";
    context.fillRect(0, 0, 320, 200);
  }

  function load()
  {
      myScale();
  }

  function drawScreen(charArray)
  {
    var char = 0;

    for (row = 0; row < 17; row++) 
    {
       for (col=0; col < 21; col++)
       {
          drawChar(charArray[char], row, col);            
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
   
     context.drawImage(imgfont, col, row, CHARSIZE, CHARSIZE, y, x, CHARSIZE, CHARSIZE); 
  }
  
  
  imgfont.addEventListener("load", load());

// EOF