using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CellularCaves
{
    class Program
    {

        // Adapted from https://gamedevelopment.tutsplus.com/tutorials/generate-random-cave-levels-using-cellular-automata--gamedev-9664

        static Random rand = new Random(6892);

        static float chanceToStartAlive = 0.4f;

        static int width = 128;
        static int height = 128;

        static int deathLimit = 3;
        static int birthLimit = 4;

        public static bool[,] initialiseMap(bool[,] map)
        {
            for (int x = 0; x < width; x++)
            {
                for (int y = 0; y < height; y++)
                {
                    if (rand.NextDouble() < chanceToStartAlive)
                    {
                        map[x,y] = true;
                    }
                }
            }
            return map;
        }

        //Returns the number of cells in a ring around (x,y) that are alive.
        public static int countAliveNeighbours(bool[,] map, int x, int y)
        {
            int count = 0;
            for(int i=-1; i<2; i++){
                for(int j=-1; j<2; j++){
                    int neighbour_x = x+i;
                    int neighbour_y = y+j;
                    //If we're looking at the middle point
                    if(i == 0 && j == 0){
                        //Do nothing, we don't want to add ourselves in!
                    }
                    //In case the index we're looking at it off the edge of the map
                    else if(neighbour_x < 0 || neighbour_y < 0 || neighbour_x >= width || neighbour_y >= height){
                        count = count + 1;
                    }
                    //Otherwise, a normal check of the neighbour
                    else if(map[neighbour_x,neighbour_y]){
                        count = count + 1;
                    }
                }
            }
            return count;
        }

        public static bool[,] doSimulationStep(bool[,] oldMap)
        {
            bool[,] newMap = new bool[width,height];

            //Loop over each row and column of the map
            for(int x=0; x<width; x++){
                for(int y=0; y<height; y++){
                    int nbs = countAliveNeighbours(oldMap, x, y);
                    //The new value is based on our simulation rules
                    //First, if a cell is alive but has too few neighbours, kill it.
                    if(oldMap[x,y]){
                        if(nbs < deathLimit){
                            newMap[x,y] = false;
                        }
                        else{
                            newMap[x,y] = true;
                        }
                    } //Otherwise, if the cell is dead now, check if it has the right number of neighbours to be 'born'
                    else{
                        if(nbs > birthLimit){
                            newMap[x,y] = true;
                        }
                        else{
                            newMap[x,y] = false;
                        }
                    }
                }
            }
            return newMap;
        }

        public static bool[,] generateMap(int numberOfSteps)
        {
            //Create a new map
            bool[,] cellmap = new bool[width,height];
            //Set up the map with random values
            cellmap = initialiseMap(cellmap);
            //And now run the simulation for a set number of steps
            for(int i=0; i<numberOfSteps; i++){
                cellmap = doSimulationStep(cellmap);
            }

            return cellmap;
        }

        //Returns the number of cells in a ring around (x,y) that are alive.
        public static String printMap(bool[,] map)
        {
            //String output = "";
            StringBuilder sb = new StringBuilder();

             //Loop over each row and column of the map
            for (int y = 0; y < height; y++)
            {                
                for (int x = 0; x < width; x++)
                {                
                    if (map[x,y]) 
                    {
                        sb.Append("#");
                    }
                    else
                    {
                        sb.Append(" ");
                    }
                }

                sb.Append(Environment.NewLine);
            }

            return sb.ToString();
        }


        static void Main(string[] args)
        {
            bool[,] myMap = generateMap(5);
            String theMap = printMap(myMap);

            File.WriteAllText(@"C:\Caves\Caves1.txt", theMap);
        }
    }
}
