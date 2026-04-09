import { useEffect, useMemo, useState } from "react";
import { getSamples, solveFunction, solveSystem } from "../api/solver";
import SolverForm from "../components/SolverForm";
import ResultPanel from "../components/ResultPanel";
import DesmosGraph from "../components/DesmosGraph";


export default function NonLinearPage() {
  const solverType = "nonlinear";

  const [mode, setMode] = useState("function");

  const [samples, setSamples] = useState({
    functions: [],
    systems: [],
    methods: { function: [], system: [] }
  });

  const [functionId, setFunctionId] = useState(0);
  const [systemId, setSystemId] = useState(0);
  const [methodId, setMethodId] = useState(0);

  const [a, setA] = useState(0);
  const [b, setB] = useState(2);
  const [epsilon, setEpsilon] = useState(1e-6);
  const [x0Text, setX0Text] = useState("0,0");

  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    let alive = true;

    getSamples(solverType)
      .then((data) => {
        if (!alive) return;
        setSamples(data);
      })
      .catch((e) => setError(e.message));

    return () => {
      alive = false;
    };
  }, []);

  const selectedFunction = useMemo(() => {
    if (mode !== "function") return "";
    return samples.functions?.[functionId] || "";
  }, [mode, samples.functions, functionId]);

  const selectedSystem = useMemo(() => {
    if (mode !== "system") return [];
    const sys = samples.systems?.[systemId];
    return Array.isArray(sys) ? sys : [];
  }, [mode, samples.systems, systemId]);

  function parseX0(text) {
  return text
    .split(/[;\s]+/) 
    .map((v) => Number(v.replace(",", "."))) 
    .filter(Number.isFinite);
  }

  async function handleSolve(e) {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const payload =
        mode === "function"
          ? {
              functionId,
              methodId,
              a: Number(a),
              b: Number(b),
              epsilon: Number(epsilon)
            }
          : {
              systemId,
              methodId,
              x0: parseX0(x0Text),
              epsilon: Number(epsilon)
            };

      const res =
        mode === "function"
          ? await solveFunction(solverType, payload)
          : await solveSystem(solverType, payload);

      setResult(res);
    } catch (err) {
      setError(err.message);
      setResult(null);
    } finally {
      setLoading(false);
    }
  }
  console.log("formula", selectedFunction);
  console.log("system", selectedSystem);
  console.log("result", result);

  return (
    <div style={{ padding: 20 }}>
      <h2>NonLinear Solver</h2>

      <SolverForm
        mode={mode}
        setMode={setMode}
        samples={samples}
        functionId={functionId}
        setFunctionId={setFunctionId}
        systemId={systemId}
        setSystemId={setSystemId}
        methodId={methodId}
        setMethodId={setMethodId}
        a={a}
        setA={setA}
        b={b}
        setB={setB}
        epsilon={epsilon}
        setEpsilon={setEpsilon}
        x0Text={x0Text}
        setX0Text={setX0Text}
        onSubmit={handleSolve}
        loading={loading}
      />

      <DesmosGraph
        mode={mode}
        formula={selectedFunction}
        system={selectedSystem}
        interval={{ a: Number(a), b: Number(b) }}
        result={result}
      />

      <ResultPanel result={result} mode={mode} error={error} />
    </div>
  );
}