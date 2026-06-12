import { useEffect, useMemo, useState } from "react";
import { getSamples, solveInterpolation } from "../api/solver";
import InterpolationForm from "../components/InterpolationForm";
import InterpolationResultPanel from "../components/InterpolationResultPanel";
import InterpolationGraph from "../components/InterpolationGraph";

function parseX(text) {
  const val = String(text).trim().replace(",", ".");
  return val === "" ? 0 : Number(val);
}

export default function InterpolationPage() {
  const [samples, setSamples] = useState({ functions: [], interpolators: [] });
  
  const [isGenerate, setIsGenerate] = useState(true);
  
  const [functionId, setFunctionId] = useState(0); 
  const [a, setA] = useState(0);
  const [b, setB] = useState(2);
  const [h, setH] = useState(0.2);
  const [targetX, setTargetX] = useState(1.0);

  const [manualPoints, setManualPoints] = useState(
    Array(5).fill(null).map(() => ({ x: "", y: "" }))
  );

  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const selectedFunction = useMemo(() => {
    if (!samples.functions || samples.functions.length === 0) return "";
    return samples.functions[functionId] || "";
  }, [samples.functions, functionId]);

  useEffect(() => {
    let alive = true;
    getSamples("interpolation") 
      .then((data) => {
        if (alive) setSamples(data);
      })
      .catch((e) => setError("Ошибка загрузки сэмплов: " + e.message));
    return () => { alive = false; };
  }, []);

  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = (event) => {
      const content = event.target.result;
      const params = content.trim().split(/[\s,;]+/).map(v => v.replace(",", "."));

      if (params.length >= 5) {
        setIsGenerate(true);
        setFunctionId(parseInt(params[0]));
        setA(params[1]);
        setB(params[2]);
        setH(params[3]);
        setTargetX(params[4]);
        setResult(null); 
      } else {
        alert("Некорректный формат файла. Нужно: ID_функции a b h targetX");
      }
      e.target.value = ""; 
    };
    reader.readAsText(file);
  };

  async function handleSolve(e) {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const payload = {
        isGenerate: isGenerate,
        functionId: Number(functionId),
        a: parseX(a),
        b: parseX(b),
        h: parseX(h),
        targetX: parseX(targetX),
        x: !isGenerate ? manualPoints.map(p => parseX(p.x)) : [],
        y: !isGenerate ? manualPoints.map(p => parseX(p.y)) : []
      };

      const res = await solveInterpolation(payload);
      setResult(res);
      
      if (!res.success) {
        setError(res.globalMessage || "Ошибка при расчете");
      }
    } catch (err) {
      setError(err.message);
      setResult(null);
    } finally {
      setLoading(false);
    }
  }

  const handleModeChange = (newMode) => {
    setIsGenerate(newMode);
    setResult(null);
    setError("");
  };

  return (
    <div style={{ padding: "20px 40px", fontFamily: "sans-serif" }}>
      <h2 style={{ borderBottom: "2px solid #28a745", paddingBottom: "10px" }}>
        Интерполяция функций (Лагранж, Ньютон, Гаусс, Стирлинг, Бессель)
      </h2>

      <div style={{ display: "flex", gap: "30px", alignItems: "flex-start", flexWrap: "wrap" }}>
        
        <div style={{ flex: "1 1 500px" }}>
          <InterpolationForm
            isGenerate={isGenerate}
            setIsGenerate={handleModeChange}
            manualPoints={manualPoints}
            setManualPoints={setManualPoints}
            samples={samples}
            functionId={functionId}
            setFunctionId={setFunctionId}
            a={a} setA={setA}
            b={b} setB={setB}
            h={h} setH={setH}
            targetX={targetX} setTargetX={setTargetX}
            onSubmit={handleSolve}
            onFileUpload={handleFileUpload}
            loading={loading}
          />
        </div>

        <div style={{ flex: "1 1 500px" }}>
          <InterpolationResultPanel result={result} error={error} />
        </div>

      </div>

      <InterpolationGraph
        originalFormula={selectedFunction}
        targetX={targetX}
        result={result}
      />
    </div>
  );
}
