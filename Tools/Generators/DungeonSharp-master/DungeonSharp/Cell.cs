using System.Collections.Generic;
using System.Diagnostics;

namespace DungeonSharp
{
    internal class Cell
    {
        internal readonly int Id;

        private readonly int m_x;
        private readonly int m_y;
        private readonly HashSet<Cell> m_connectedTo = new HashSet<Cell>();

        public Cell(int x, int y, int id)
        {
            m_x = x;
            m_y = y;
            Id = id;
        }

        public bool Connected { get; set; }

        public void ConnectTo(Cell other)
        {
            m_connectedTo.Add(other);
            other.m_connectedTo.Add(this);
            Connected = true;
            other.Connected = true;

            Debug.WriteLine(string.Format("Connecting {0} -> {1}", new Coordinate(m_x, m_y), new Coordinate(other.m_x, other.m_y)));
        }

        public int X
        {
            get { return m_x; }
        }

        public int Y
        {
            get { return m_y; }
        }

        public Room Room { get; set; }

        public bool Equals(Cell other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return other.Id == Id;
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof(Cell)) return false;
            return Equals((Cell)obj);
        }

        public override int GetHashCode()
        {
            return Id;
        }

        public override string ToString()
        {
            return "Cell" + new Coordinate(m_x, m_y);
        }
    }
}
