import Link from "next/link";

export default function NotFound() {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center gap-4 px-8 text-center bg-background">
      <p className="text-lg font-bold text-gray-800">Restaurante não encontrado</p>
      <p className="text-sm text-primary-text">O restaurante que você procura não existe ou foi removido.</p>
      <Link
        href="/home"
        className="px-6 py-2.5 bg-primary text-white rounded-xl text-sm font-semibold hover:bg-primary/90 transition-colors"
      >
        Voltar ao início
      </Link>
    </div>
  );
}
