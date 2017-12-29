using System;
using System.Collections.Generic;

namespace DungeonSharp
{
    internal class Randomizer
    {
        private readonly Random m_random;

        public Randomizer()
            : this(DateTime.Now.Millisecond)
        {
        }

        public Randomizer(int seed)
        {
            m_random = new Random(seed);
        }

        public int Next(int min, int max)
        {
            return m_random.Next(min, max);
        }

        public Cell Select(CellGrid grid)
        {
            int index = m_random.Next(grid.Count);
            return grid[index];
        }

        public T Select<T>(ICollection<T> collection)
        {
            int index = m_random.Next(collection.Count);

            var list = collection as IList<T>;
            if (list != null)
            {
                return list[index];
            }

            foreach (var pair in collection)
            {
                if (index-- <= 0)
                    return pair;
            }

            throw new InvalidOperationException();
        }
    }
}
