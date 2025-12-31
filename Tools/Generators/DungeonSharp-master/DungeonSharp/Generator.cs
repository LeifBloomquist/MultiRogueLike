using System;
using System.Collections.Generic;
using System.Linq;

namespace DungeonSharp
{
    public class Generator
    {
        public static Grid<Tile> Generate(int cellsX, int cellsY, int cellSize = 5)
        {
            var random = new Randomizer(1268);

            // 1. Divide the map into a grid of evenly sized cells.
            var cells = new CellGrid(cellsX, cellsY);

            // 2. Pick a random cell as the current cell and mark it as connected.
            var current = random.Select(cells);
            current.Connected = true;

            // 3. While the current cell has unconnected neighbor cells:
            while (true)
            {
                cells.GetNeighborCells(current).ToList();
                var unconnected = cells.GetNeighborCells(current).Where(x => !x.Connected).ToList();
                if (unconnected.Count == 0)
                    break;

                // 3a. Connect to one of them.
                var neighbor = random.Select(unconnected);
                current.ConnectTo(neighbor);

                // 3b. Make that cell the current cell.
                current = neighbor;
            }

            // 4. While there are unconnected cells:
            while (cells.Count(x => !x.Connected) > 0)
            {
                // 4a. Pick a random connected cell with unconnected neighbors and connect to one of them.
                var candidates = new List<Tuple<Cell, List<Cell>>>();
                foreach (var cell in cells.Where(x => x.Connected))
                {
                    var unconnectedNeighbors = cells.GetNeighborCells(cell).Where(x => !x.Connected).ToList();
                    if (unconnectedNeighbors.Count == 0)
                        continue;
                    candidates.Add(new Tuple<Cell, List<Cell>>(cell, unconnectedNeighbors));
                }
                var pair = random.Select(candidates);
                var neighbor = random.Select(pair.Item2);
                pair.Item1.ConnectTo(neighbor);
            }

            // TODO: 5. Pick zero or more pairs of adjacent cells that are not connected and connect them.

            // 6. Within each cell, create a room of random shape.
            foreach (var cell in cells)
            {
                var room = new Room
                {
                    Width = random.Next(3, cellSize - 2),
                    Height = random.Next(3, cellSize - 2)
                };
                room.X = (cell.X * cellSize) + random.Next(1, cellSize - room.Width - 1);
                room.Y = (cell.Y * cellSize) + random.Next(1, cellSize - room.Height - 1);
                cell.Room = room;
            }

            // 7. For each connection between two cells:
            var corridors = new List<IRoom>();
            foreach (var connection in cells.GetCellConnections())
            {
                // 7a. Create a random corridor between the rooms in each cell.
                var floors1 = connection.Item1.Room.GetFloorTiles().ToList();
                var floors2 = connection.Item2.Room.GetFloorTiles().ToList();

                var start = random.Select(floors1);
                var end = random.Select(floors2);

                // basic line drawing algorithm
                var corridor = new List<Coordinate>();
                var i = new Coordinate(start);
                while (i != end)
                {
                    var dx = Math.Abs(i.X - end.X);
                    var dy = Math.Abs(i.Y - end.Y);

                    if (dx > dy)
                    {
                        if (i.X < end.X)
                            i.X++;
                        else
                            i.X--;
                    }
                    else
                    {
                        if (i.Y < end.Y)
                            i.Y++;
                        else
                            i.Y--;
                    }
                    corridor.Add(new Coordinate(i));
                }

                // only use tiles that are not already used by any of the two rooms
                corridors.Add(new Corridor(
                    corridor.Where(x => !floors1.Contains(x) && !floors2.Contains(x)).ToList()));
            }
            // 8. Place staircases in the cell picked in step 2 and the lest cell visited in step 3ii.)

            var tiles = new TileGrid(cellsX * cellSize, cellsY * cellSize);
            foreach (var floor in cells
                .Select(x => x.Room)
                .Where(room => room != null)
                .SelectMany(room => room.GetFloorTiles()))
            {
                tiles[floor.X, floor.Y] = Tile.Floor;
            }
            foreach (var floor in corridors.SelectMany(corridor => corridor.GetFloorTiles()))
            {
                tiles[floor.X, floor.Y] = Tile.Floor;
            }

            // turn every empty tile adjacent to a floor into a wall
            for (int x = 0; x < tiles.SizeX; x++)
            {
                for (int y = 0; y < tiles.SizeY; y++)
                {
                    if (tiles[x, y] != Tile.Empty)
                        continue;

                    if (tiles.GetAdjacentEightWay(x, y).Contains(Tile.Floor))
                        tiles[x, y] = Tile.Wall;
                }
            }

            // TODO would be useful to return Cell collection as well
            return tiles;
        }
    }
}
