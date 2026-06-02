package solver.interpolation.samples

object Samples {
  val functions = List(
    "2*x + 3",              
    "x^2 - 4*x + 1",        
    "0.5*x^3 - 2*x^2 + x",  
    "exp(0.3*x)",           
    "2*exp(0.5*x) + 1",     
    "ln(x)",                
    "3*ln(x) + 2",          
    "x^0.5",                
    "2*x^1.7 + 1",          
    "sin(x)",               
    "cos(2*x)",             
    "1/(1+x^2)",            
    "sqrt(x)",              
    "abs(x)",               
    "x^3 - 6*x^2 + 4*x + 12"
  )

  val interpolators = List(
    "Многочлен Лагранжа",
    "Ньютон (раздел. разности)",
    "Ньютон (конечн. разности)",
    "Многочлен Гаусса",
    "Схема Стирлинга",
    "Схема Бесселя"
  )
}
