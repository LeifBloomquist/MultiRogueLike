using System.Collections.Generic;

namespace DungeonSharp
{
    internal interface IRoom
    {
        IEnumerable<Coordinate> GetFloorTiles();
    }
}
