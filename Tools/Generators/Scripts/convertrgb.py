from PIL import Image

# Set the width and height of the ASCII art
ASCII_WIDTH = 128
ASCII_HEIGHT = 128

# Open the PNG image using the PIL library
image = Image.open("test.png")

# Assume already 128x128

# Get the pixel values of the image
pixels = list(image.getdata())

# Convert each pixel to an ASCII character based on its color
ascii_art = ""

for i in range(len(pixels)):
    pixel = pixels[i]
    if pixel == (0,0,0):         #Solid
        ascii_art += "."
    elif pixel == (187,187,187): # Room 1
        ascii_art += " "
    elif pixel == (119,119,119): # Room 2
        ascii_art += " "
    elif pixel == (170,170,170): # Room 3
        ascii_art += " "
    elif pixel == (221,221,221): # Room 3
        ascii_art += " "
    elif pixel == (85,85,85):    # Wall
        ascii_art += "#"
    elif pixel == (34,102,153):  # Wall (blue maps)
        ascii_art += "#"
    elif pixel == (204,204,85):  # Yellow Door
        ascii_art += "d"
    elif pixel == (119,68,0):    # Red Door
        ascii_art += "d"
    elif pixel == (204,204,0):   # Yellow, Not sure??
        ascii_art += "y"
    elif pixel == (102,102,102): # Cell doors
        ascii_art += "D"
    else:
        print('Unknown pixel ', pixel)
        ascii_art += "Z"
    
    if (i+1) % ASCII_WIDTH == 0:
        ascii_art += "\n"

# Print the ASCII art
print(ascii_art)