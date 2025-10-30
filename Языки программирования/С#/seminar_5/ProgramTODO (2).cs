// https://raw.githubusercontent.com/sahildit/IMDB-Movies-Extensive-Dataset-Analysis/refs/heads/master/data1/IMDb%20movies.csv
using System.Runtime.CompilerServices;
using Microsoft.VisualBasic.FileIO;
using System.Globalization;

List<string[]> ReadAllCsvLines(string filePath)
{
    var result = new List<string[]>();

    using (var parser = new TextFieldParser(filePath))
    {
        parser.TextFieldType = FieldType.Delimited;
        parser.SetDelimiters(",");
        parser.HasFieldsEnclosedInQuotes = true;

        while (!parser.EndOfData)
        {
            string[] fields = parser.ReadFields();
            result.Add(fields);
        }
    }

    return result;
}

var splistr = "A, B, C".Split(',');


var records = ReadAllCsvLines("IMDb movies.csv");


// 1. Преобразовать в класс Movie (нужно создать класс) - Select
var movie = records
    .Skip(1)
    .Select(columns => new Movie
    {
        ImdbTitleId = columns[0],
        Title = columns[1],
        OriginalTitle = columns[2],
        Year = int.TryParse(columns[3], out var year) ? year : 0,
        DatePublished = (columns[4]),
        Genre = columns[5].Split(',').ToList(),
        Duration = int.TryParse(columns[6], out var duration) ? duration : 0,
        Country = columns[7],
        Language = columns[8],
        Director = columns[9].Split(',').ToList(),
        Writer = columns[10].Split(',').ToList(),
        ProductionCompany = columns[11],
        Actors = columns[12].Split(',').ToList(),
        Description = columns[13],
        AvgVote = double.TryParse(columns[14], NumberStyles.Any, CultureInfo.InvariantCulture, out var avgVote) ? avgVote : 0,
        Votes = int.TryParse(columns[15], out var votes) ? votes : 0,
        Budget = columns[16],
        UsaGrossIncome = columns[17],
        WorldwideGrossIncome = columns[18],
        Metascore = float.TryParse(columns[19], NumberStyles.Any, CultureInfo.InvariantCulture, out var metascore) ? metascore : 0,
        ReviewsFromUsers = float.TryParse(columns[20], NumberStyles.Any, CultureInfo.InvariantCulture, out var reviewsFromUsers) ? reviewsFromUsers : 0,
        ReviewsFromCritics = float.TryParse(columns[21], NumberStyles.Any, CultureInfo.InvariantCulture, out var reviewsFromCritics) ? reviewsFromCritics : 0,
    })
    .ToList();

Console.WriteLine(movie.Count());
// Над списком из Movie 
// 2. Найти все фильмы режисёра (на выбор, например Nolan) - Where

var result2 = movie
    .Where(movie => movie.Director.Any(d => d.Equals("Christopher Nolan", StringComparison.InvariantCultureIgnoreCase)))
    .ToList();

foreach (var m in result2)
{
    Console.WriteLine($"{m.Title} ({m.Year}) — Рейтинг: {m.AvgVote}");
    Console.WriteLine($"Режиссёры: {string.Join(", ", m.Director)}");
    Console.WriteLine($"Актёры: {string.Join(", ", m.Actors)}");
    Console.WriteLine(new string('-', 40));
}

Console.WriteLine(new string('#', 40));






// 3. 5 самый высокооценённых фильма выпущенных после 2010 

var maxScore = movie.Max(m => m.Metascore);
var result3 = movie
    .Where(movie => movie.Year > 2010)
    .OrderByDescending(m => m.AvgVote)
    .Take(5)
    .ToList();

foreach (var m in result3)
{
    Console.WriteLine($"{m.Title} ({m.Year}) — Рейтинг: {m.AvgVote}");
    Console.WriteLine($"Режиссёры: {string.Join(", ", m.Director)}");
    Console.WriteLine($"Актёры: {string.Join(", ", m.Actors)}");
    Console.WriteLine(new string('-', 40));
}

Console.WriteLine(new string('#', 40));






// 4. Получить список фильмов (их количество и средний рейтинг) жанра (на выбор, например Drama) 

var count = movie.Where(movie => movie != null && movie.Genre.Any(g => g.Trim().Equals("drama", StringComparison.OrdinalIgnoreCase)))
    .Select(movie => movie.AvgVote)
    .Count();

var avg = movie.Where(movie => movie.Genre.Any(g => g.Trim().Equals("drama", StringComparison.InvariantCultureIgnoreCase)))
    .Average(movie => movie.AvgVote);

Console.WriteLine(count);
Console.WriteLine(avg);
Console.WriteLine(new string('#', 40));







// 5. Режисёр у которого больше всего фильмов
var result5 = movie
    .SelectMany(movie => movie.Director, (movie, director) => new { movie, director })
    .GroupBy(x => x.director, x => x.movie)
    .OrderByDescending(g => g.Count())
    .First();

Console.WriteLine($"Режиссёры: {string.Join(", ", result5.Key)}");

// Не учитывать регистр