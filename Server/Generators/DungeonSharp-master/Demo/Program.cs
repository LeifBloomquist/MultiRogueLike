using DungeonSharp;

namespace Demo
{
    internal class Program
    {
        private static void Main()
        {
            Grid<Tile> tiles = Generator.Generate(50, 50, 20);
            Renderer.Render(tiles);
        }
    }
}
