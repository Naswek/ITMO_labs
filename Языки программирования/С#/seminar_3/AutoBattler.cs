using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CSConsoleApp
{
    public class AutoBattler
    {

        private List<Hero> heroes = new();
        public AutoBattler(int heroCount)
        {
            if (heroCount <= 0)
                throw new ArgumentException("Hero count must be greater than zero.", nameof(heroCount));

            for (int i = 0; i < heroCount; i++)
            {
                heroes.Add(new Fairy());
                heroes.Add(new Wizard());
            }
        }

        public void StartBattle()
        {
            var random = new Random();
            int count = 2;
            while (heroes.Count(h => h.HP > 0) > 1)
            {
                var attacker = heroes.Where(h => h.HP > 0).OrderBy(_ => random.Next()).First();
                var target = heroes.Where(h => h.HP > 0 && h != attacker).OrderBy(_ => random.Next()).FirstOrDefault();


                //attacker.ApplySpecialAbility();
                if (target != null)
                {
                    attacker.Attack(target);
                    if (count % 3 == 0)
                    {
                        attacker.ApplySpecialAbility(attacker);
                    }

                    if (count % 5 == 0)
                    {
                        target.UseArtifact();
                    }
                }
                count++;
            }

            var winner = heroes.FirstOrDefault(h => h.HP > 0);
            if (winner != null)
            {
                ConsoleUtils.Logger($"{winner.Name} is the last hero standing with {winner.HP} HP left!", ConsoleColor.Green);
            }
            else
            {
                ConsoleUtils.Logger("All heroes have fallen!", ConsoleColor.DarkRed);
            }
        }
    }
}
