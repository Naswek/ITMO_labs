using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CSConsoleApp
{

    public abstract class Hero : IArtifact
    {
        
        public Hero(string heroName, int hp, int attackPower, int defense)
        {
            if (string.IsNullOrWhiteSpace(heroName))
                throw new ArgumentException("Name cannot be null or empty.", nameof(heroName));
            var random = new Random();
            Name = heroName + $" №{random.Next(0, 10)}";
            HP = hp;
            AttackPower = attackPower;
            Defense = defense;
        }

        public string Name { get; set; }

        public int HP { get; protected set; }

        public int AttackPower { get; set; }

        public int Defense { get; set; }

        public IArtifact Artifact { get; set; }

        public virtual void UseArtifact()
        {
            AttackPower += 5;
        }


        public void Attack(Hero target)
        {
            int damage = AttackPower - target.Defense;

            if (damage < 0)
                damage = 0;

            target.TakeDamage(damage);

            ConsoleUtils.Logger($"{Name} attacks {target.Name} for {damage} damage!", ConsoleColor.Yellow);
        }

        public void TakeDamage(int damage)
        {
            HP -= damage;

            if (this is Wizard)
            {
                Console.Beep(500, 100);
            }
            else if (this is Fairy)
            {
                Console.Beep(300, 100);
            }

            if (HP < 0) 
                HP = 0;

            ConsoleUtils.Logger($"{Name} takes {damage} damage! Remaining HP: {HP}", ConsoleColor.Red);
        }

        public void IgnoreResistance(int attackPower)
        {
            if (attackPower < 0)
                throw new ArgumentException("Attack power cannot be negative.", nameof(attackPower));

            AttackPower += attackPower;
        }

        public void TakeHeal(int value)
        {
            if (value < 0)
            {
                throw new ArgumentException("extra HP power cannot be negative.", nameof(value));
            }

            HP += value;

        }

        public abstract void ApplySpecialAbility(params Hero[] hero);

        public void Use(Hero hero)
        {
            hero.HP += 20;
        }

    }
}
