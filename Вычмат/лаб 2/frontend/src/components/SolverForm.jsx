import React from "react";

export default function SolverForm({
  mode, setMode, samples,
  functionId, setFunctionId, systemId, setSystemId, methodId, setMethodId,
  a, setA, b, setB, epsilon, setEpsilon, x0Text, setX0Text,
  onSubmit, loading
}) {
  const methodsList = mode === "function" ? samples.methods?.function || [] : samples.methods?.system || [];

  const parseNum = (val) => Number(String(val).trim().replace(",", "."));

  const isValidNum = (val) => {
    const s = parseNum(val);
    return s !== "" && s !== "-" && s !== "." && !isNaN(Number(s)) ;
  };

  const isValidX0 = (val) => {
    const parts = String(val).trim().split(/[;\s]+/).filter(Boolean);
    return parts.length > 0 && parts.every(isValidNum);
  };


  const isValidEpsilon = isValidNum(epsilon) && parseNum(epsilon) > 0 && parseNum(epsilon) < 1;

  const isAandBValid = parseNum(a) < parseNum(b);

  const inputStyle = (isValid) => ({
    border: isValid ? "1px solid #ccc" : "2px solid #ff4d4f",
    backgroundColor: isValid ? "#fff" : "#fff1f0",
    outline: "none",
    padding: "6px 8px",
    borderRadius: "4px"
  });

  const isFormValid = mode === "function"
    ? isValidNum(a) && isValidNum(b) && isValidEpsilon
    : isValidX0(x0Text) && isAandBValid ;

  const handleChange = (setter) => (e) => setter(e.target.value);

  return (
    <form
      onSubmit={onSubmit}
      style={{
        padding: 20,
        border: "1px solid #ddd",
        borderRadius: 12,
        display: "grid",
        gap: 12,
        maxWidth: 520
      }}
    >
      <h3 style={{ margin: 0 }}>Solver</h3>

      <div style={{ display: "flex", gap: 8, flexWrap: "wrap" }}>
        <button type="button" onClick={() => setMode("function")} disabled={mode === "function"}>
          Function
        </button>
        <button type="button" onClick={() => setMode("system")} disabled={mode === "system"}>
          System
        </button>
      </div>

      {mode === "function" ? (
        <select value={functionId} onChange={(e) => setFunctionId(Number(e.target.value))}>
          {samples.functions.map((f, i) => (
            <option key={i} value={i}>
              {f}
            </option>
          ))}
        </select>
      ) : (
        <select value={systemId} onChange={(e) => setSystemId(Number(e.target.value))}>
          {samples.systems.map((s, i) => (
            <option key={i} value={i}>
              {Array.isArray(s) ? `${s[0]} | ${s[1]}` : String(s)}
            </option>
          ))}
        </select>
      )}

      <select value={methodId} onChange={(e) => setMethodId(Number(e.target.value))}>
        {methodsList.map((m, i) => (
          <option key={i} value={i}>
            {m}
          </option>
        ))}
      </select>

      {mode === "function" ? (
        <>
          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 12 }}>
            <input
              value={a}
              onChange={handleChange(setA)}
              placeholder="a (start)"
              style={inputStyle(isValidNum(a))}
            />
            <input
              value={b}
              onChange={handleChange(setB)}
              placeholder="b (end)"
              style={inputStyle(isValidNum(b))}
            />
          </div>

          {!isAandBValid && (
            <div style={inputStyle(false)}> 
              b должно быть больше a
            </div>
          )}
        </>
      ) : (
        <input
          value={x0Text}
          onChange={handleChange(setX0Text)}
          placeholder="x0 (e.g. 1.5; 2.5)"
          style={inputStyle(isValidX0(x0Text))}
          title={!isValidX0(x0Text) ? "Введите числа через пробел или точку с запятой" : ""}
        />
      )}  

      <input
        value={epsilon}
        onChange={handleChange(setEpsilon)}
        placeholder="epsilon (e.g. 1e-6)"
        style={inputStyle(isValidEpsilon)}
        title={!isValidEpsilon ? "Введите корректную точность" : ""}
      />

      <button type="submit" disabled={loading || !isFormValid}>
        {loading ? "Solving..." : "Solve"}
      </button>
    </form>
  );
}