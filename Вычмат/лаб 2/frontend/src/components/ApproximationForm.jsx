import React from "react";

export default function ApproximationForm({
  samples,       
  functionId,    
  setFunctionId, 
  a, setA,       
  b, setB,       
  h, setH,       
  onSubmit,      
  loading        
}) {

  const parseNum = (val) => Number(String(val).trim().replace(",", "."));

  const isValidNum = (val) => {
    const n = parseNum(val);
    return val !== "" && !isNaN(n);
  };

  const isAandBValid = isValidNum(a) && isValidNum(b) && parseNum(a) < parseNum(b);
  const isHValid = isValidNum(h) && parseNum(h) > 0;

  const isFormValid = isValidNum(a) && isValidNum(b) && isAandBValid && isHValid;

  const inputStyle = (isValid) => ({
    border: isValid ? "1px solid #ccc" : "2px solid #ff4d4f",
    backgroundColor: isValid ? "#fff" : "#fff1f0",
    outline: "none",
    padding: "6px 8px",
    borderRadius: "4px",
    width: "100%"
  });

  const handleChange = (setter) => (e) => setter(e.target.value);

  const pointsCount = isFormValid 
    ? Math.floor((parseNum(b) - parseNum(a)) / parseNum(h)) + 1 
    : 0;

  return (
    <form
      onSubmit={onSubmit}
      style={{
        padding: 20,
        border: "1px solid #ddd",
        borderRadius: 12,
        display: "grid",
        gap: 12,
        maxWidth: 600,
        marginBottom: 20
      }}
    >
      <h3 style={{ margin: 0 }}>Настройки аппроксимации</h3>

      <div style={{ display: "flex", flexDirection: "column", gap: 4 }}>
        <label style={{ fontSize: "12px", color: "gray" }}>Выберите функцию для табулирования</label>
        <select 
          value={functionId} 
          onChange={(e) => setFunctionId(Number(e.target.value))}
          style={{ padding: "6px", borderRadius: "4px" }}
        >
          {samples.functions.map((func, index) => (
            <option key={index} value={index}>
              {func}
            </option>
          ))}
        </select>
      </div>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr 1fr", gap: 12 }}>
        <div style={{ display: "flex", flexDirection: "column", gap: 4 }}>
          <label style={{ fontSize: "12px", color: "gray" }}>Нижняя граница (a)</label>
          <input
            value={a}
            onChange={handleChange(setA)}
            placeholder="0"
            style={inputStyle(isValidNum(a))}
          />
        </div>

        <div style={{ display: "flex", flexDirection: "column", gap: 4 }}>
          <label style={{ fontSize: "12px", color: "gray" }}>Верхняя граница (b)</label>
          <input
            value={b}
            onChange={handleChange(setB)}
            placeholder="2"
            style={inputStyle(isValidNum(b) && isAandBValid)}
          />
        </div>

        <div style={{ display: "flex", flexDirection: "column", gap: 4 }}>
          <label style={{ fontSize: "12px", color: "gray" }}>Шаг (h)</label>
          <input
            value={h}
            onChange={handleChange(setH)}
            placeholder="0.2"
            style={inputStyle(isHValid)}
          />
        </div>
      </div>

      
      {!isAandBValid && isValidNum(a) && isValidNum(b) && (
        <div style={{ color: "#cf1322", fontSize: "12px" }}>
           Граница <b>b</b> должна быть строго больше <b>a</b>
        </div>
      )}
      {!isHValid && h !== "" && (
        <div style={{ color: "#cf1322", fontSize: "12px" }}>
           Шаг <b>h</b> должен быть больше нуля
        </div>
      )}


      <button type="submit" disabled={loading || !isFormValid}>
        {loading ? "Решаем..." : "Найти аппроксимацию"}
      </button>

      {isFormValid && (
        <div style={{ fontSize: "0.85rem", color: "#52c41a", textAlign: "center" }}>
          Будет сгенерировано <b>{pointsCount}</b> точек для МНК.
        </div>
      )}
    </form>
  );
}