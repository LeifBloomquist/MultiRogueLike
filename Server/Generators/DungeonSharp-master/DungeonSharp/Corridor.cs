using System.Collections.Generic;

namespace DungeonSharp
{
    internal class Corridor : IRoom
    {
        private readonly List<Coordinate> m_floorTiles;

        public Corridor(List<Coordinate> floorTiles)
        {
            m_floorTiles = floorTiles;
        }

        #region Implementation of IRoom

        public IEnumerable<Coordinate> GetFloorTiles()
        {
            return m_floorTiles;
        }

        #endregion
    }
}
