using System;
using System.Collections.Generic;
using System.Linq;

namespace DungeonSharp
{
    internal class CellGrid : Grid<Cell>
    {
        public CellGrid(int sizeX, int sizeY)
            : base(sizeX, sizeY)
        {
            for (int y = 0; y < sizeY; y++)
            {
                for (int x = 0; x < sizeX; x++)
                {
                    int index = CoordToIndex(x, y);
                    Items[index] = new Cell(x, y, index);
                }
            }
        }

        public IEnumerable<Cell> GetNeighborCells(Cell cell)
        {
            return GetAdjacentFourWay(cell.X, cell.Y);
        }

        /// <summary>
        /// Get every connection between every cell.
        /// Each Connection is only counted once.
        /// </summary>
        /// <returns></returns>
        public IEnumerable<Tuple<Cell, Cell>> GetCellConnections()
        {
            // build a hash set of cell id pairs, counting each connection only once
            var set = new HashSet<Tuple<int, int>>();

            foreach (var cell in Items)
            {
                foreach (var neighbor in GetNeighborCells(cell))
                {
                    set.Add(cell.Id > neighbor.Id
                        ? new Tuple<int, int>(cell.Id, neighbor.Id)
                        : new Tuple<int, int>(neighbor.Id, cell.Id));
                }
            }

            return set.Select(tuple => new Tuple<Cell, Cell>(Items[tuple.Item1], Items[tuple.Item2]));
        }
    }
}
