import { useEffect, useMemo, useState } from "react";
import { getSamples, solveApproximation } from "../api/solver";
import ApproximationForm from "../components/ApproximationForm";
import ApproximationResultPanel from "../components/ApproximationResultPanel";
import ApproximationGraph from "../components/ApproximationGraph";

function parseX(text) {
  return Number(String(text).trim().replace(",", "."));
}

export default function ApproximationPage() {
  const [samples, setSamples] = useState({ functions: [], approximators: [] });
  
  const [functionId, setFunctionId] = useState(0); 
  const [a, setA] = useState(0);
  const [b, setB] = useState(2);
  const [h, setH] = useState(0.2);

  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const selectedFunction = useMemo(() => {
    if (!samples.functions || samples.functions.length === 0) return "";
    const func = samples.functions[functionId];
    return typeof func === 'object' ? func.name : func; 
  }, [samples.functions, functionId]);

  useEffect(() => {
    let alive = true;
    getSamples("approximation") 
      .then((data) => {
        if (alive) setSamples(data);
      })
      .catch((e) => setError(e.message));
    return () => { alive = false; };
  }, []);

  async function handleSolve(e) {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const realId = samples.functions[functionId]?.id || (functionId);

      const payload = {
        functionId: realId, 
        a: parseX(a),
        b: parseX(b),
        h: parseX(h)
      };

      const res = await solveApproximation(payload);
      setResult(res);
    } catch (err) {
      setError(err.message);
      setResult(null);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ padding: 20 }}>
      <h2>Approximation Solver (МНК)</h2>

      <ApproximationForm
        samples={samples}
        functionId={functionId}
        setFunctionId={setFunctionId}
        a={a} setA={setA}
        b={b} setB={setB}
        h={h} setH={setH}
        onSubmit={handleSolve}
        loading={loading}
      />

      <ApproximationGraph
        originalFormula={selectedFunction}
        result={result}
      />

      <ApproximationResultPanel result={result} error={error} />
    </div>
  );
}