public static class ConsoleUtils
{
    public static void Logger(string log, ConsoleColor color)
    {
        Console.ForegroundColor = color;
        Console.WriteLine(log);
        Console.ResetColor();

        File.AppendAllText(Path.Combine(Environment.CurrentDirectory, "log.txt"), log + "\n");
    }

    public static void Beep()
    {
        Console.Beep();
    }
}