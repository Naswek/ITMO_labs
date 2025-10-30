using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CSConsoleApp
{
    public sealed class Wizard : Hero
    {
        public Wizard() : base("Wizard", 100, 40, 5)
        {

        }

        public override void ApplySpecialAbility(params Hero[] heroes)
        {
            Random random = new Random();
            foreach (var hero in heroes)
            {
                Console.Beep();
                int extraAttackPower = random.Next(10, 15);
                hero.IgnoreResistance(extraAttackPower);
                ConsoleUtils.Logger($"{Name} is ignoring resistance and add + {extraAttackPower} AttackPower. Current AttackPower: {AttackPower}", ConsoleColor.Blue);
            }
        }

        public override void UseArtifact()
        {
            AttackPower += 10;
        }
    }
}
