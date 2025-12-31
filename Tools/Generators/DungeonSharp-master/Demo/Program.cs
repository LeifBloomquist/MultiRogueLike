using DungeonSharp;

namespace Demo
{
    internal class Program
    {
        private static void Main()
        {
            Grid<Tile> tiles = Generator.Generate(8, 8, 16);
            Renderer.Render(tiles);
        }
    }
}
