"use client";

import { useEffect } from "react";

// Registra o service worker no cliente. Componente sem UI.
export default function ServiceWorkerRegister() {
  useEffect(() => {
    if (process.env.NODE_ENV !== "production") return; // evita cache atrapalhar dev
    if (!("serviceWorker" in navigator)) return;
    navigator.serviceWorker.register("/sw.js").catch((err) => {
      console.error("Falha ao registrar service worker:", err);
    });
  }, []);

  return null;
}
