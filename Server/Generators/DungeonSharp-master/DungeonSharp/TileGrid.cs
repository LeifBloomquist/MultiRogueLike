namespace DungeonSharp
{
    internal class TileGrid : Grid<Tile>
    {
        public TileGrid(int sizeX, int sizeY)
            : base(sizeX, sizeY)
        {
            for (int y = 0; y < sizeY; y++)
                for (int x = 0; x < sizeX; x++)
                    Items[CoordToIndex(x, y)] = Tile.Empty;
        }
    }
}
