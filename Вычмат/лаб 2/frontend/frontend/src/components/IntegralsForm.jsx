import React from "react";

export default function IntegralsForm({
  samples,
  integralId, setIntegralId,
  methodId, setMethodId,
  a, setA, b, setB, epsilon, setEpsilon,
  onSubmit, loading
}) {
  

  const parseNum = (val) => Number(String(val).trim().replace(",", "."));

  const isValidNum = (val) => {
    const s = String(val).trim().replace(",", ".");
    return s !== "" && s !== "-" && s !== "." && !isNaN(Number(s));
  };

  const isValidEpsilon = isValidNum(epsilon) && parseNum(epsilon) > 0 && parseNum(epsilon) < 1;
  const isAandBValid = isValidNum(a) && isValidNum(b) && parseNum(a) < parseNum(b);

  const inputStyle = (isValid) => ({
    border: isValid ? "1px solid #ccc" : "2px solid #ff4d4f",
    backgroundColor: isValid ? "#fff" : "#fff1f0",
    outline: "none",
    padding: "6px 8px",
    borderRadius: "4px",
    width: "100%",
    boxSizing: "border-box"
  });

  const isFormValid = isValidNum(a) && isValidNum(b) && isValidEpsilon && isAandBValid;

  return (
    <form
      onSubmit={onSubmit}
      style={{
        padding: 20,
        border: "1px solid #ddd",
        borderRadius: 12,
        display: "grid",
        gap: 12,
        maxWidth: 520,
        marginBottom: 20
      }}
    >
      <h3 style={{ margin: 0 }}>Настройки интеграла</h3>

      <label style={{ fontSize: "14px", fontWeight: "bold", marginBottom: "-8px" }}>Выберите функцию:</label>
      <select value={integralId} onChange={(e) => setIntegralId(Number(e.target.value))}>
        {samples.integrals?.map((f, i) => (
          <option key={i} value={i}>
            {f}
          </option>
        ))}
      </select>

      <label style={{ fontSize: "14px", fontWeight: "bold", marginBottom: "-8px" }}>Выберите метод:</label>
      <select value={methodId} onChange={(e) => setMethodId(Number(e.target.value))}>
        {samples.methods?.integrals?.map((m, i) => (
          <option key={i} value={i}>
            {m}
          </option>
        ))}
      </select>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 12 }}>
        <div>
          <label style={{ fontSize: "12px", color: "gray" }}>Нижний предел (a)</label>
          <input
            value={a}
            onChange={(e) => setA(e.target.value)}
            style={inputStyle(isValidNum(a))}
          />
        </div>
        <div>
          <label style={{ fontSize: "12px", color: "gray" }}>Верхний предел (b)</label>
          <input
            value={b}
            onChange={(e) => setB(e.target.value)}
            style={inputStyle(isValidNum(b))}
          />
        </div>
      </div>


      {!isAandBValid && isValidNum(a) && isValidNum(b) && (
        <div style={{ color: "#ff4d4f", fontSize: "13px", marginTop: "-8px" }}>
          Верхний предел (b) должен быть больше нижнего (a)
        </div>
      )}

      <div>
        <label style={{ fontSize: "12px", color: "gray" }}>Точность (epsilon)</label>
        <input
          value={epsilon}
          onChange={(e) => setEpsilon(e.target.value)}
          placeholder="например: 1e-4"
          style={inputStyle(isValidEpsilon)}
        />
      </div>

      <button type="submit" disabled={loading || !isFormValid} style={{ padding: "8px", cursor: "pointer" }}>
        {loading ? "Вычисляем..." : "Вычислить интеграл"}
      </button>
    </form>
  );
}