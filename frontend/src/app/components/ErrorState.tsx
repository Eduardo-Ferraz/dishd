"use client";

interface ErrorStateProps {
  reset: () => void;
  message?: string;
}

/** Estado de erro reutilizavel pelos error.tsx das rotas (client, expoe reset()). */
export default function ErrorState({ reset, message }: ErrorStateProps) {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center gap-4 px-8 text-center bg-background">
      <p className="text-sm text-primary-text">
        {message ?? "Não foi possível carregar os dados. Verifique sua conexão e tente novamente."}
      </p>
      <button
        onClick={reset}
        className="px-6 py-2.5 bg-primary text-white rounded-xl text-sm font-semibold hover:bg-primary/90 transition-colors"
      >
        Tentar novamente
      </button>
    </div>
  );
}
