using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using part01;
using System.Reflection.Emit;

namespace part01
{
    [GameAttribute]
    public class AngelMehaknics 
    {

        [CombatSkill("God's protection", TriggerType.OnDefense, 100)]
        public void ExecuteLifeDrain(BattleContext ctx)
        {
            int discreased = CreateDynamicMethod1()(ctx.DamageDealt, 0.5f);
            ctx.DamageDealt = discreased;
            Console.WriteLine($"[System] God's protection activation: damage dicreased for {discreased}.");
        }

        [CombatSkill("Fatality", TriggerType.PostBattle, 10)]
        public void ExecuteCrit(BattleContext ctx)
        {
            Console.WriteLine($"[System] CRITICAL HIT! Damage doubled. {ctx.Attacker.Hp} {ctx.Defender.Hp}");
            int healDicreaseAtt = (int) CreateDynamicMethod2()(ctx.Attacker.Hp, 20);
            int healDicreaseDeff = (int)CreateDynamicMethod2()(ctx.Defender.Hp, 20);
            ctx.Attacker.Hp = healDicreaseAtt;
            ctx.Defender.Hp = healDicreaseDeff;
            Console.WriteLine($"[System] CRITICAL HIT! Damage doubled. {healDicreaseAtt} {healDicreaseDeff}");
        }
        public static Func<int, float, int> CreateDynamicMethod1()
        {
            // 1. Создаем "Метод-призрак" (DynamicMethod)
            // Имя: "FastCalc"
            // Возвращает: double
            // Аргументы: double, double, double
            var dynamicMethod = new DynamicMethod("FastCalc", typeof(int), new[] { typeof(int), typeof(float)});

            // 2. Получаем ILGenerator — это наш "ассемблер" для .NET
            ILGenerator il = dynamicMethod.GetILGenerator();

            // Пишем программу на стековой машине:

            // Загружаем аргумент 0 (a) в стек
            il.Emit(OpCodes.Ldarg_0);

            il.Emit(OpCodes.Conv_R4);

            // Загружаем аргумент 1 (b) в стек
            il.Emit(OpCodes.Ldarg_1);
            il.Emit(OpCodes.Conv_R4);

            // Складываем два верхних числа в стеке (a + b) -> результат падает в стек
            il.Emit(OpCodes.Mul);

            // Загружаем аргумент 2 (c) в стек
            //il.Emit(OpCodes.Ldarg_2);

            // Умножаем (result * c) -> результат в стеке
            //il.Emit(OpCodes.Mul);

            // Возвращаем то, что лежит на вершине стека
            il.Emit(OpCodes.Conv_I);
            il.Emit(OpCodes.Ret);

            //il.Emit(OpCodes.Div);
            //il.Emit(OpCodes.Ldc, 10)
            //il.Emit(OpCodes.Ldc_I4, 10);

            // 3. "Компилируем" это в делегат C#
            return (Func<int, float, int>)dynamicMethod.CreateDelegate(typeof(Func<int, float, int>));
        }


        public static Func<int, int, int> CreateDynamicMethod2()
        {
            // 1. Создаем "Метод-призрак" (DynamicMethod)
            // Имя: "FastCalc"
            // Возвращает: double
            // Аргументы: double, double, double
            var dynamicMethod = new DynamicMethod("FastCalc", typeof(int), new[] { typeof(int), typeof(int) });

            // 2. Получаем ILGenerator — это наш "ассемблер" для .NET
            ILGenerator il = dynamicMethod.GetILGenerator();

            // Пишем программу на стековой машине:

            // Загружаем аргумент 0 (a) в стек
            il.Emit(OpCodes.Ldarg_0);

            // Загружаем аргумент 1 (b) в стек
            il.Emit(OpCodes.Ldarg_1);

            // Складываем два верхних числа в стеке (a + b) -> результат падает в стек
            il.Emit(OpCodes.Sub);

            // Возвращаем то, что лежит на вершине стека
            il.Emit(OpCodes.Ret);

            //il.Emit(OpCodes.Div);
            //il.Emit(OpCodes.Ldc, 10)
            //il.Emit(OpCodes.Ldc_I4, 10);

            // 3. "Компилируем" это в делегат C#
            return (Func<int, int, int>)dynamicMethod.CreateDelegate(typeof(Func<int, int, int>));
        }



    }
}

