public class Movie
{
    public string ImdbTitleId { get; set; }
    public string Title { get; set; }
    public string OriginalTitle { get; set; }
    public int Year { get; set; }
    public string DatePublished { get; set; }
    public List<string> Genre { get; set; }
    public int Duration { get; set; }
    public string Country { get; set; }
    public string Language { get; set; }
    public List<string>Director { get; set; }
    public List<string> Writer { get; set; }
    public string ProductionCompany { get; set; }
    public List<string> Actors { get; set; }
    public string Description { get; set; }
    public double AvgVote { get; set; }
    public int Votes { get; set; }
    public string Budget { get; set; }
    public string UsaGrossIncome { get; set; }
    public string WorldwideGrossIncome { get; set; }
    public float Metascore { get; set; }
    public float ReviewsFromUsers { get; set; }
    public float ReviewsFromCritics { get; set; }
}

