using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.IO;

namespace CSVConverter
{
    class Program
    {
        static void Main(string[] args)
        {
            string prefix = @"C:\Leif\GitHub\MultiRogueLike\Server\data\test\";
            String[] csv_lines = File.ReadAllLines(prefix + "LevelTest0.csv");
            
            string output = "";

            foreach (String line in csv_lines)
            {
                String[] vals = line.Split(',');

                foreach (String val in vals)
                {
                    int iv = Int32.Parse(val);

                    switch (iv)
                    {
                        case 0:
                            output += " ";
                            break;

                        case 1:
                            output += "#";
                            break;

                        case 2:
                            output += ".";
                            break;

                        default:
                            output += "?";
                            break;
                    }
                }
                output += "\r\n";
            }
            File.WriteAllText(prefix + "LevelTest0.txt", output);
        }
    }
}
