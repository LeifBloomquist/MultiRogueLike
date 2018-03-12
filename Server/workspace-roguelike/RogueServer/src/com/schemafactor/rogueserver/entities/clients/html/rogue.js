// rogue.js

   var imgfont = document.createElement("img");
   imgfont.src = "c64font.png";
   
   var actiontext = document.getElementById("action");

   var screen = document.getElementById("screen");                       
   var context = screen.getContext("2d");
   
   context.imageSmoothingEnabled = false;
   context.mozImageSmoothingEnabled = false;
   context.webkitImageSmoothingEnabled = true;
   context.msImageSmoothingEnabled = true;
   
   function toHexString(byteArray) 
   {
        return Array.from(byteArray, function(byte) 
        {
             return ('0' + (byte & 0xFF).toString(16)).slice(-2);
        }).join(' ');
   }

   function WebSocketTest()
   {
      if ("WebSocket" in window)
      {
         document.getElementById("demo").innerHTML += "WebSocket is supported by your Browser!";
         
         context.fillStyle = "green";
         context.fillRect(10, 10, 100, 50);
         
         // Let us open a web socket
         var ws = new WebSocket("ws://localhost:3007/Rogue");         
         ws.binaryType = 'arraybuffer';
	
         ws.onopen = function()
         {
            // Web Socket is connected, send data using send()
            ws.send("Message to send");
            document.getElementById("demo").innerHTML += "<p>Message is sent...";
         };
	
         ws.onmessage = function (evt) 
         { 
            var byteArray = new Uint8Array(evt.data);           
            document.getElementById("hex").innerHTML = toHexString(byteArray);
            drawScreen(byteArray.slice(30,357)); 
         };
	
         ws.onclose = function()
         { 
            // websocket is closed.
            alert("Connection is closed..."); 
         };
		
         window.onbeforeunload = function(event) {
            socket.close();
         };
      }
      
      else
      {
         // The browser doesn't support WebSocket
         alert("WebSocket NOT supported by your Browser!");
      }
   }


  document.onkeydown = function(e) 
  { 
    switch (e.keyCode) 
    { 
        case 33:
           moveUp();
           break; 

        case 34:
           moveDown();
           break; 

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


  function moveUp()
  {
     action.innerHTML = "Move Up";
  }

  function moveDown()
  {
     action.innerHTML = "Move Down";
  }

  function moveNorth()
  {
     action.innerHTML = "Move North";
  }

  function moveSouth()
  {
     action.innerHTML = "Move South";
  }

  function moveWest()
  {
     action.innerHTML = "Move West";
  }

  function moveEast()
  {
     action.innerHTML = "Move East";
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
    context.fillStyle = "grey"; 
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
          drawChar(charArray[char], col, row);
          char++;
       }
    }
  }

  function drawChar(num, x, y)
  {
     const FONTWIDTH = 16;
     const CHARSIZE  = 8;
     
     var col = num % FONTWIDTH;
     var row = Math.floor(num/FONTWIDTH);
     
     col *= CHARSIZE;
     row *= CHARSIZE;

     x *= CHARSIZE;
     y *= CHARSIZE;  
   
     context.drawImage(imgfont, col, row, CHARSIZE, CHARSIZE, x, y, CHARSIZE, CHARSIZE); 
  }
  
  imgfont.addEventListener("load", load());

// EOF