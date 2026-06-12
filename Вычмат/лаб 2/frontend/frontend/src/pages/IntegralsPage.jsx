import { useEffect, useState } from "react";
import { getSamples, solveIntegral } from "../api/solver";
import ResultPanel from "../components/ResultPanel";
import IntegralsForm from "../components/IntegralsForm";

function parseX(text) {
  return Number(String(text).trim().replace(",", "."));
}

export default function IntegralsPage() {
  const [samples, setSamples] = useState({ integrals: [], methods: { integrals: [] } });
  
  const [integralId, setIntegralId] = useState(0);
  const [methodId, setMethodId] = useState(0);
  const [a, setA] = useState("0");
  const [b, setB] = useState("2");
  const [epsilon, setEpsilon] = useState("0.001");

  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    let alive = true;
    getSamples("integrals") 
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
      const payload = {
        integralId,
        methodId,
        a: parseX(a),
        b: parseX(b),
        epsilon: parseX(epsilon)
      };

      const res = await solveIntegral(payload);
      setResult(res);
    } catch (err) {
      setError(err.message);
      setResult(null);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <h2>Численное интегрирование</h2>
      <IntegralsForm
        samples={samples}
        integralId={integralId} setIntegralId={setIntegralId}
        methodId={methodId} setMethodId={setMethodId}
        a={a} setA={setA}
        b={b} setB={setB}
        epsilon={epsilon} setEpsilon={setEpsilon}
        onSubmit={handleSolve}
        loading={loading}
      />
      <ResultPanel result={result} mode="integral" error={error} />
    </div>
  );
}