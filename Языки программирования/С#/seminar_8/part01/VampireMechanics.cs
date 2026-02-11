using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace part01
{
    [GameAttribute]
    public class VampireMechanics
    {
        // Декларативная разметка: этот метод должен сработать при атаке с приоритетом 10
        [CombatSkill("Vampirism", TriggerType.OnAttack, 50)]
        public void ExecuteLifeDrain(BattleContext ctx)
        {
            // Логика: 50% урона возвращается в виде здоровья
            int healAmount = (int)(ctx.DamageDealt * 0.5);

            ///ctx.Attacker.Hp = IlMethod(ctx.Hp, healAmount);
            ctx.Attacker.Hp += healAmount;
            Console.WriteLine($"[System] Vampirism activation: healed {healAmount} HP.");
        }

        [CombatSkill("CriticalStrike", TriggerType.OnAttack, 50)]
        public void ExecuteCrit(BattleContext ctx)
        {
            //ctx.DamageDealt = IlMethod(ctx.DamageDealt, 2);
            ctx.DamageDealt *= 2;
            Console.WriteLine($"[System] CRITICAL HIT! Damage doubled.");
        }
    }
}
