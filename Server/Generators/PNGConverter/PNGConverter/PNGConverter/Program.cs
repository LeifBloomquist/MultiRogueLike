using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.IO;

namespace PNGConverter
{
    class Program
    {
        static void Main(string[] args)
        {
            string prefix = @"C:\Leif\GitHub\MultiRogueLike\Server\data\Conway\";
            Bitmap img = new Bitmap(prefix + "maze100.png");
            string output = "";

            for (int i = 0; i < img.Width; i++)
            {
                for (int j = 0; j < img.Height; j++)
                {
                    Color pixel = img.GetPixel(i,j);

                    if (pixel.R > 100)
                    {
                        output += " ";
                    }
                    else
                    {
                        output += "#";
                    }
                }
                output += "\r\n";
            }

            File.WriteAllText(prefix + "Conwayblob100.txt", output);
        }
    }
} 
