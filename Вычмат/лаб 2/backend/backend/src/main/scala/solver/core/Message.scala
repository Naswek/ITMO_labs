package solver.core

enum Message:

  case Success
  case BadParameters   
  case BadId
  case ServerError     

  case MethodDoesNotConverge   
  case IterationLimitExceeded 
  case NoRootInInterval       
  case DivisionByZero          

  case MaxPartitionsReached
  case IntegralDoesNotExist
  case IntervalWasChanged 
 
  case NotEnoughPoints
  case InvalidDataForModel
  case SingularMatrix 
  
  case FunctionUndefined
  case InvalidInterval
  case TooManyPoints