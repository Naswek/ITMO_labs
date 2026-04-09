export function adaptSamples(raw) {
  return {
    functions: Array.isArray(raw?.functions) ? raw.functions : [],
    systems: Array.isArray(raw?.systems) ? raw.systems : [],
    methods: {
      function: Array.isArray(raw?.methods?.function) ? raw.methods.function : [],
      system: Array.isArray(raw?.methods?.system) ? raw.methods.system : []
    }
  };
}

export function adaptFunctionResult(raw) {
  const data = raw?.data ?? raw;

  return {
    x: data?.x ?? 0,
    fx: data?.fx ?? 0,
    iterations: data?.iterations ?? data?.iter ?? 0,
    message: data?.message ?? "OK",
    dataPoints: normalizeDataPoints(data)
  };
}

export function adaptSystemResult(raw) {
  const data = raw?.data ?? raw;

  return {
    x: Array.isArray(data?.x) ? data.x : [],
    fx: Array.isArray(data?.fx) ? data.fx : [],
    iterations: data?.iterations ?? data?.iter ?? 0,
    message: data?.message ?? "OK"
  };
}

function normalizeDataPoints(raw) {
  const dp = raw?.dataPoints;

  if (Array.isArray(dp)) {
    return dp.map((p) => ({
      x: Array.isArray(p.x) ? p.x[0] : p.x,
      fx: p.fx ?? 0
    }));
  }

  if (raw?.x !== undefined && raw?.fx !== undefined) {
    return [{ x: raw.x, fx: raw.fx }];
  }

  return [];
}