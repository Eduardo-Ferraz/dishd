/** Spinner de pagina, usado nos loading.tsx (Suspense) das rotas de dados. */
export default function PageSpinner() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-background">
      <div className="w-8 h-8 border-2 border-secundary border-t-primary rounded-full animate-spin" />
    </div>
  );
}
