using part01;
using System.Reflection;

// Рефлексия — возможность в рантайме изучать/модифицировать внутренние структуры 
// программы.

// Рефлексия в C/C++ — компайл тайм.

// Требует сохранение метаинформации про типы/функции.

// С => asm x86
// С# => IL (Intermediate Language)
// IL => dotnet
// Go => asm + type

// https://docs.google.com/spreadsheets/d/1utaXjCi8hMj_o5YO5WN7D34mD_cHC2WnKTg743QW3sc/edit?usp=sharing

// 1. Атрибуты
// 2. Получить доступ к приватному методу
// 3. Когда это реально нужно


// TODO:
// 1. Создадим класс с атрибутом GameAttribute
// 2. У этого класса создать методы спсобности с атрибутом CombatSkill 
// 3. Один метод OnDefense другой PostBattle
// 4. Вызвать новые пайплайны в порядке OnDefense=>OnAttack=>PostBattle

// 5. Метод с OnDefense уменьшат атаку в два раза 

// 6. Метод c PostBattle уменьшает у Атакующего и Защищающегося HP на 20
// 7. Пункты 5 и 6 выполнить с помощью ILGenerator (всю математику)
// (Не использовать Call, CallVirt, инструкцию)
// Damage = Damage / 2 (не делаем) => il.Emit() (делаем)

var engine = new SkillEngine();


// Регистрация текущей сборки (или загрузка внешней DLL)
engine.RegisterAssembly(Assembly.GetExecutingAssembly());

// Симуляция контекста боя
var context = new BattleContext
{
    DamageDealt = 100,
    Attacker = new UnitStats { Hp = 50 },
    Defender = new UnitStats { Hp = 100 }
};

Console.WriteLine("--- Starting Attack Phase ---");

// Ядро само находит нужные методы и вызывает их в правильном порядке (сначала Crit, потом Vampirism)
engine.ExecutePipeline(TriggerType.OnDefense, context);
engine.ExecutePipeline(TriggerType.OnAttack, context);
engine.ExecutePipeline(TriggerType.PostBattle, context);

Console.WriteLine($"Attacker Final HP: {context.Attacker.Hp}"); // 50 + 100 (Crit * 2 * 0.5) = 150

