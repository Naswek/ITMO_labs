using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;

[DllImport("point_lib.dll", CallingConvention = CallingConvention.Cdecl)]
static extern Point filter([In, Out] Point[] points, [In] int count, [Out] Point[] results, map f);   



// 3.1 Массив точек
// 3.2 Их количество
// 3.3 Массив точек для хранения результата
// 3.4 Функцию фильтрации
// Функция записывает в результат(3.3) отфильтрованные точки 
// Функции задаются в C#
// 4. Применить функцию разложить точки на координатные четверти


var array_points = new Point[1001];

String[] lines = System.IO.File.ReadAllLines("output.txt");  
for (int i = 0; i < lines.Length; i++)
{
    var parts = lines[i].Split(' ');
    array_points[i] = new Point {
        x = int.Parse(parts[0]),
        y = int.Parse(parts[1])
    };
    System.Console.WriteLine($"Point {i}: ({parts[0]}, {parts[1]})");
}

var result = new Point[array_points.Length];

for (int i = 0; i < result.Length; i++)
{
    
}


map func = p => new Point
{
    x = p.x > 500 ? 0 : p.x,
    y = p.y > 500 ? 0 : p.y
};




filter(array_points, array_points.Length, result, func);



[StructLayout(LayoutKind.Sequential)]
public struct Point
{
    public int x;
    public int y;
}

public delegate Point map(Point p);



