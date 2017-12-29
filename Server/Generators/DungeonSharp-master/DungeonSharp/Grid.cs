using System.Collections;
using System.Collections.Generic;

namespace DungeonSharp
{
    public class Grid<T> : IEnumerable<T>
    {
        private readonly int m_sizeX;
        private readonly int m_sizeY;

        protected readonly T[] Items;

        public Grid(int sizeX, int sizeY)
        {
            m_sizeX = sizeX;
            m_sizeY = sizeY;
            Items = new T[sizeX * sizeY];
        }

        public int Count
        {
            get { return Items.Length; }
        }

        public int SizeX
        {
            get { return m_sizeX; }
        }

        public int SizeY
        {
            get { return m_sizeY; }
        }

        public bool IsInBounds(int x, int y)
        {
            int index = CoordToIndex(x, y);
            return IsInBounds(index);
        }

        // All adjacent elements excluding diagonal
        internal IEnumerable<T> GetAdjacentFourWay(int x, int y)
        {
            var offsets = new[]
            {
                new Coordinate(-1, 0),
                new Coordinate(0, -1),
                new Coordinate(1, 0),
                new Coordinate(0, 1),
            };

            foreach (var offset in offsets)
            {
                var ox = x + offset.X;
                if (ox < 0 || ox >= SizeX)
                    continue;
                var oy = y + offset.Y;
                if (oy < 0 || oy >= SizeY)
                    continue;
                yield return this[ox, oy];
            }
        }

        // All adjacent elements including diagonal
        internal IEnumerable<T> GetAdjacentEightWay(int x, int y)
        {
            var offsets = new[]
            {
                new Coordinate(-1, -1),
                new Coordinate(0, -1),
                new Coordinate(1, -1),
                new Coordinate(-1, 0),
                new Coordinate(1, 0),
                new Coordinate(-1, 1),
                new Coordinate(0, 1),
                new Coordinate(1, 1),
            };

            foreach (var offset in offsets)
            {
                var ox = x + offset.X;
                if (ox < 0 || ox >= SizeX)
                    continue;
                var oy = y + offset.Y;
                if (oy < 0 || oy >= SizeY)
                    continue;
                yield return this[ox, oy];
            }
        }

        private bool IsInBounds(int index)
        {
            return index >= 0 && index < Items.Length;
        }

        protected int CoordToIndex(int x, int y)
        {
            return (y * m_sizeX) + x;
        }

        public T this[int index]
        {
            get { return Items[index]; }
            set { Items[index] = value; }
        }

        public T this[int x, int y]
        {
            get { return Items[CoordToIndex(x, y)]; }
            set { Items[CoordToIndex(x, y)] = value; }
        }

        #region Implementation of IEnumerable

        public IEnumerator<T> GetEnumerator()
        {
            return ((IEnumerable<T>)Items).GetEnumerator();
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return GetEnumerator();
        }

        #endregion
    }
}
