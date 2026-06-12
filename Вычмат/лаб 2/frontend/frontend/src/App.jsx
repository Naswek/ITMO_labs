import { BrowserRouter, Navigate, Route, Routes, NavLink } from "react-router-dom";
import NonLinearPage from "./pages/NonLinearPage";
import IntegralsPage from "./pages/IntegralsPage";
import ApproximationPage from "./pages/ApproximationPage";
import InterpolationPage from "./pages/InterpolationPage";
import React from 'react';

export default function App() {
  
  const linkStyle = ({ isActive }) => ({
    padding: "10px 20px",
    textDecoration: "none",
    color: isActive ? "white" : "black",
    backgroundColor: isActive ? "#007bff" : "#e9ecef",
    borderRadius: "8px",
    fontWeight: "bold",
    transition: "0.2s"
  });
  
  return (
    <BrowserRouter>
      <nav style={{ display: "flex", gap: 10, marginBottom: 20, borderBottom: "2px solid #eee", paddingBottom: 15 }}>
        <NavLink to="/nonlinear" style={linkStyle}>Лаб 2 (Уравнения)</NavLink>
        <NavLink to="/integrals" style={linkStyle}>Лаб 3 (Интегралы)</NavLink>
        <NavLink to="/approximation" style={linkStyle}>Лаб 4 (Аппроксимации)</NavLink>
        <NavLink to="/interpolation" style={linkStyle}>Лаб 5 (Интерполяция)</NavLink>
      </nav>
      <Routes>
        <Route path="/nonlinear" element={<NonLinearPage />} />
        <Route path="/integrals" element={<IntegralsPage />} />
        <Route path="/approximation" element={<ApproximationPage />} />
        <Route path="/interpolation" element={<InterpolationPage />} />
        <Route path="*" element={<Navigate to="/nonlinear" replace />} />
      </Routes>
    </BrowserRouter>
  );
}