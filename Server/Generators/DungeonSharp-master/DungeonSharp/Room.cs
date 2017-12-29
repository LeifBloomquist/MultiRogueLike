using System.Collections.Generic;

namespace DungeonSharp
{
    internal class Room : IRoom
    {
        public int X;
        public int Y;
        public int Width;
        public int Height;

        public IEnumerable<Coordinate> GetFloorTiles()
        {
            for (int y = 0; y < Height; y++)
                for (int x = 0; x < Width; x++)
                    yield return new Coordinate(X + x, Y + y);
        }
    }
}
