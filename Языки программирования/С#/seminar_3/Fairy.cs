using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CSConsoleApp
{
    public class Fairy : Hero
    {
        public Fairy() : base("Fairy", 150, 30, 10)
        {

        }

        public override void ApplySpecialAbility(params Hero[] heroes)
        {
            Random random = new Random();
            foreach (var hero in heroes)
            {
                Console.Beep();
                int extraAttackPower = random.Next(10, 15);
                hero.TakeHeal(extraAttackPower);
                ConsoleUtils.Logger($"{Name} takes heal.", ConsoleColor.Blue);
            }
        }

        public override void UseArtifact()
        {
            HP += 10;
        }
    }
}
