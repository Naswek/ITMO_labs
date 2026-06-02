import React from "react";

export default function ApproximationForm({
  isGenerate, setIsGenerate, 
  manualPoints, setManualPoints,
  samples,
  functionId, setFunctionId,
  a, setA,
  b, setB,
  h, setH,
  onSubmit,
  onFileUpload,
  loading
}) {
  const parseNum = (val) => Number(String(val).trim().replace(",", "."));
  const isValidNum = (val) => val !== "" && !isNaN(parseNum(val)) && val !== "-";

  const numA = parseNum(a);
  const numB = parseNum(b);
  const numH = parseNum(h);
  const isAandBValid = isValidNum(a) && isValidNum(b) && numA < numB;
  const isHValid = isValidNum(h) && numH > 0;

  const pointsCount = isGenerate 
    ? (isAandBValid && isHValid ? Math.floor((numB - numA) / numH) + 1 : 0)
    : manualPoints.length;

  const isPointsCountValid = pointsCount >= 8 && pointsCount <= 12;

  const isManualDataValid = !isGenerate && manualPoints.every(p => isValidNum(p.x) && isValidNum(p.y));

  const isFormValid = isGenerate 
    ? (isAandBValid && isHValid && isPointsCountValid)
    : (isPointsCountValid && isManualDataValid);

  const inputStyle = (isValid) => ({
    border: isValid ? "1px solid #ccc" : "2px solid #ff4d4f",
    backgroundColor: isValid ? "#fff" : "#fff1f0",
    outline: "none",
    padding: "8px",
    borderRadius: "6px",
    width: "100%"
  });

  const btnStyle = (active) => ({
    flex: 1,
    padding: "10px",
    cursor: "pointer",
    backgroundColor: active ? "#007bff" : "#e9ecef",
    color: active ? "white" : "black",
    border: "none",
    borderRadius: "8px",
    fontWeight: "bold",
    transition: "0.2s"
  });

  const updatePoint = (index, field, value) => {
    const newPoints = [...manualPoints];
    newPoints[index][field] = value;
    setManualPoints(newPoints);
  };

  const removePoint = (index) => {
    setManualPoints(manualPoints.filter((_, i) => i !== index));
  };

  const addPoint = () => {
    if (manualPoints.length < 12) {
      setManualPoints([...manualPoints, { x: "", y: "" }]);
    }
  };

  return (
    <div style={{ 
      padding: 20, 
      border: "1px solid #ddd", 
      borderRadius: 12, 
      maxWidth: 600, 
      backgroundColor: "#fff",
      boxShadow: "0 4px 10px rgba(0,0,0,0.05)"
    }}>
      <h3 style={{ marginTop: 0 }}>Настройки аппроксимации</h3>

  
      <div style={{ display: "flex", gap: 10, marginBottom: 20 }}>
        <button type="button" onClick={() => setIsGenerate(true)} style={btnStyle(isGenerate)}>
          Из функции
        </button>
        <button type="button" onClick={() => setIsGenerate(false)} style={btnStyle(!isGenerate)}>
          Ввод таблицы
        </button>
      </div>

      <form onSubmit={onSubmit} style={{ display: "grid", gap: 15 }}>
        
        {isGenerate ? (
          <>
            <div style={{ display: "flex", flexDirection: "column", gap: 5 }}>
              <label style={{ fontSize: "12px", color: "gray" }}>Выберите функцию</label>
              <select 
                value={functionId} 
                onChange={(e) => setFunctionId(Number(e.target.value))}
                style={{ padding: "8px", borderRadius: "6px" }}
              >
                {samples.functions.map((f, i) => (
                  <option key={i} value={i}>{f}</option>
                ))}
              </select>
            </div>

            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr 1fr", gap: 10 }}>
              <div>
                <label style={{ fontSize: "12px", color: "gray" }}>Нижняя (a)</label>
                <input value={a} onChange={e => setA(e.target.value)} style={inputStyle(isValidNum(a))} />
              </div>
              <div>
                <label style={{ fontSize: "12px", color: "gray" }}>Верхняя (b)</label>
                <input value={b} onChange={e => setB(e.target.value)} style={inputStyle(isValidNum(b) && isAandBValid)} />
              </div>
              <div>
                <label style={{ fontSize: "12px", color: "gray" }}>Шаг (h)</label>
                <input value={h} onChange={e => setH(e.target.value)} style={inputStyle(isHValid)} />
              </div>
            </div>

            <label style={{ cursor: "pointer", color: "#28a745", fontSize: "13px", fontWeight: "bold" }}>
              📂 Загрузить параметры (id a b h)
              <input type="file" accept=".txt" onChange={onFileUpload} style={{ display: "none" }} />
            </label>
          </>
        ) : (
          <div style={{ display: "grid", gap: 8 }}>
            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr 40px", gap: 10, paddingBottom: 5, borderBottom: "1px solid #eee" }}>
              <span style={{ fontSize: "12px", color: "gray" }}>Координата X</span>
              <span style={{ fontSize: "12px", color: "gray" }}>Координата Y</span>
              <span></span>
            </div>
            <div style={{ maxHeight: "250px", overflowY: "auto", display: "grid", gap: 8, paddingRight: "5px" }}>
              {manualPoints.map((p, i) => (
                <div key={i} style={{ display: "grid", gridTemplateColumns: "1fr 1fr 40px", gap: 10 }}>
                  <input value={p.x} onChange={e => updatePoint(i, "x", e.target.value)} placeholder="X" style={inputStyle(isValidNum(p.x))} />
                  <input value={p.y} onChange={e => updatePoint(i, "y", e.target.value)} placeholder="Y" style={inputStyle(isValidNum(p.y))} />
                  <button type="button" onClick={() => removePoint(i)} style={{ border: "none", background: "none", cursor: "pointer", color: "#ff4d4f", fontSize: "18px" }}>✖</button>
                </div>
              ))}
            </div>
            <button 
              type="button" 
              onClick={addPoint} 
              disabled={manualPoints.length >= 12}
              style={{ padding: "8px", borderRadius: "6px", cursor: manualPoints.length >= 12 ? "not-allowed" : "pointer", border: "1px dashed #007bff", color: "#007bff", backgroundColor: "transparent" }}
            >
              + Добавить точку ({manualPoints.length}/12)
            </button>
          </div>
        )}

        <div style={{ 
          textAlign: "center", 
          padding: "10px", 
          borderRadius: "8px", 
          backgroundColor: isPointsCountValid ? "#f6ffed" : "#fff1f0",
          color: isPointsCountValid ? "#52c41a" : "#cf1322",
          fontSize: "14px",
          border: `1px solid ${isPointsCountValid ? "#b7eb8f" : "#ffa39e"}`
        }}>
          Итого точек: <b>{pointsCount}</b> 
          {!isPointsCountValid && " (нужно от 8 до 12 штук)"}
        </div>

        <button 
          type="submit" 
          disabled={loading || !isFormValid} 
          style={{ 
            padding: "12px",  
            fontWeight: "bold", 
            fontSize: "16px",
            cursor: isFormValid ? "pointer" : "not-allowed"
          }}
        >
          {loading ? "Выполняем расчет..." : "Найти аппроксимацию"}
        </button>
      </form>
    </div>
  );
}