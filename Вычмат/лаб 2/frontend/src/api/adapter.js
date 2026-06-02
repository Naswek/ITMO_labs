export function translateMessage(msg) {
  const dictionary = {
    "Success": "Успешно",
    "BadId": "Неверный ID",
    "BadParameters": "Ошибка ввода параметров",
    "MethodDoesNotConverge": "Метод расходится",
    "IterationLimitExceeded": "Превышено максимальное количество итераций (1000)",
    "NoRootInInterval": "В данном промежутке нет корня",
    "DivisionByZero": "Предупреждение: деление на ноль / производная близка к нулю",
    "MaxPartitionsReached": "Достигнут предел разбиений (точность не гарантируется)",
    "IntegralDoesNotExist": "Интеграл не существует (расходится)",
    "IntervalWasChanged": "Границы интервала были незначитально изменены для обхода точки разрыва",
    "NotEnoughPoints" : "Не хватило точек для применения метода",
    "InvalidDataForModel" : "Невалидные данные для модели",
    "SingularMatrix" : "Определитель матрицы равен нулю",
    "FunctionUndefined": "Функция не определена на заданном интервале",
    "InvalidInterval": "Неверные параметры интервала (a должно быть <= b, h должно быть > 0)",
  };
  return dictionary[msg] || msg;
}

export function adaptSamples(raw) {
  return {
    functions: Array.isArray(raw?.functions) ? raw.functions : [],
    systems: Array.isArray(raw?.systems) ? raw.systems : [],
    integrals: Array.isArray(raw?.integrals) ? raw.integrals : [],
    methods: {
      function: Array.isArray(raw?.methods?.function) ? raw.methods.function : [],
      system: Array.isArray(raw?.methods?.system) ? raw.methods.system : [],
      integrals: Array.isArray(raw?.methods) ? raw.methods : [],
      approximators: Array.isArray(raw?.methods) ? raw.methods : [],
      interpolators: Array.isArray(raw?.methods) ? raw.methods : []
    }
  };
}

export function adaptFunctionResult(raw) {
  return {
    x: raw?.x ?? 0,
    fx: raw?.fx ?? 0,
    iterations: raw?.iterations ?? 0,
    message: translateMessage(raw?.message),
    isError: raw?.message === "NoRootInInterval" || raw?.message === "MethodDoesNotConverge"
  };
}

export function adaptSystemResult(raw) {
  return {
    x: Array.isArray(raw?.x) ? raw.x : [],
    fx: Array.isArray(raw?.fx) ? raw.fx : [],
    iterations: raw?.iterations ?? 0,
    message: translateMessage(raw?.message)
  };
}

export function adaptIntegralResult(raw) {
  return {
    value: raw?.value ?? 0,
    n: raw?.n ?? 0,
    message: translateMessage(raw?.message),
    isError: raw?.message === "IntegralDoesNotExist"
  };
}

export function adaptApproximationResult(raw) {
  return {
    success: raw?.success ?? false,
    bestMethod: raw?.bestMethod ?? "",
    globalMessage: translateMessage(raw?.message),
    sourcePoints: Array.isArray(raw?.sourcePoints) ? raw.sourcePoints : [],
    
    results: Array.isArray(raw?.results) ? raw.results.map(res => ({
      methodName: res.methodName,
      equation: res.equation,
      mse: typeof res.mse === 'number' ? res.mse.toFixed(4) : "—",
      rSquared: typeof res.rSquared === 'number' ? res.rSquared.toFixed(4) : "—",
      pearson: typeof res.pearson === 'number' ? res.pearson.toFixed(4) : "—",
      message: translateMessage(res.message),
      isSuccess: res.message === "Success",
      coefficients: res.coefficients ?? [],
    })) : []
  };
}

export function adaptInterpolationResult(raw) {
  return {
    success: raw?.success ?? false,
    bestMethod: raw?.bestMethod ?? "",
    globalMessage: translateMessage(raw?.message),
    sourcePoints: Array.isArray(raw?.sourcePoints) ? raw.sourcePoints : [],
    
    results: Array.isArray(raw?.results) ? raw.results.map(res => ({
      methodName: res.methodName,
      value: typeof res.value === 'number' ? res.value : null,
      differenceTable: Array.isArray(res.differenceTable) ? res.differenceTable : [],
      message: translateMessage(res.message),
      isSuccess: res.message === "Success",
      isExtrapolated: res.isExtrapolated ?? false
    })) : []
  };
}