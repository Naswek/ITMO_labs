import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import NonLinearPage from "./pages/NonLinearPage";
import React from 'react';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/nonlinear" element={<NonLinearPage />} />
        <Route path="*" element={<Navigate to="/nonlinear" replace />} />
      </Routes>
    </BrowserRouter>
  );
}