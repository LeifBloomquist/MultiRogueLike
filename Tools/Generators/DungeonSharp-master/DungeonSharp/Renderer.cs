using System;
using System.Collections.Generic;
using System.Diagnostics;

namespace DungeonSharp
{
    public class Renderer
    {
        public static void Render(Grid<Tile> tiles)
        {
            for (int y = 0; y < tiles.SizeY; y++)
            {
                for (int x = 0; x < tiles.SizeX; x++)
                    Write(TileToChar(tiles[x, y]));
                Write('\n');
            }
        }

        private static char TileToChar(Tile tile)
        {
            switch (tile)
            {
                case Tile.Empty:
                    return ' ';
                case Tile.Floor:
                    return '.';
                case Tile.Wall:
                    return '#';
                default:
                    throw new KeyNotFoundException(tile.ToString());
            }
        }

        private static void Write(char s)
        {
            Console.Write(s);
            Debug.Write(s);
        }
    }
}
