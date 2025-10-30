using System;

namespace CSConsoleApp
{
    static class Program
    {
        public static void Main(string[] args)
        {
//            var heroes = GetHeroes();
//            foreach (var hero in heroes)
//            {
//                ConsoleUtils.Logger($"{hero.Name} - HP: {hero.HP}, Attack: {hero.AttackPower}, Defense: {hero.Defense}", ConsoleColor.DarkBlue);
//            }

            var battleManager = new AutoBattler(5);
            battleManager.StartBattle();
            Console.ReadKey();
        }

        public static IEnumerable<Hero> GetHeroes()
        {
            var heroes = new List<Hero>();
            for (int i = 0; i < 3; i++)
            {
                heroes.Add(new Fairy());
                heroes.Add(new Wizard());
            }

            return heroes;
        }
    }
}