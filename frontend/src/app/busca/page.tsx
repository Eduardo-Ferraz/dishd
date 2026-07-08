import Image from "next/image";
import Link from "next/link";
import Navbar from "../components/Navbar";
import BottomNav from "../components/BottomNav";
import SearchBox from "../components/SearchBox";
import StarRating from "../components/StarRating";
import { api } from "../lib/api";
import { fallbackImg } from "../lib/images";
import type { PagedResponse, RestauranteDTO } from "../lib/types";

interface PageProps {
  searchParams: Promise<{ q?: string }>;
}

export default async function BuscaPage({ searchParams }: PageProps) {
  const { q } = await searchParams;
  const query = q?.trim() ?? "";

  const path = query
    ? `/api/restaurantes?busca=${encodeURIComponent(query)}&size=30`
    : "/api/restaurantes?size=30";
  const resp = await api<PagedResponse<RestauranteDTO>>(path);
  const results = resp.content;

  return (
    <main className="min-h-screen bg-background flex flex-col relative">
      <Navbar />

      <div className="px-4 pt-4 pb-24">
        <SearchBox initialQuery={query} />

        <p className="text-xs text-primary-text mt-4 mb-2 font-medium">
          {query ? `${results.length} resultado(s) para "${query}"` : "Sugestões"}
        </p>

        {results.length === 0 ? (
          <p className="text-sm text-primary-text text-center mt-10">Nenhum restaurante encontrado.</p>
        ) : (
          <div className="flex flex-col divide-y divide-secundary">
            {results.map((r) => (
              <Link key={r.id} href={`/restaurante/${r.id}`} className="flex gap-3 py-3 hover:opacity-80 transition-opacity">
                <div className="relative w-16 h-16 shrink-0 rounded-xl overflow-hidden">
                  <Image src={fallbackImg(r.fotoUrl, r.id)} alt={r.nome} fill className="object-cover" />
                </div>
                <div className="flex-1 min-w-0">
                  <span className="text-sm font-semibold text-gray-800">{r.nome}</span>
                  <p className="text-xs text-primary-text mt-0.5">{r.endereco ?? "Endereço não informado"}</p>
                  <div className="flex items-center gap-2 mt-1">
                    <StarRating rating={Math.round(r.notaMedia)} />
                    <span className="text-xs text-primary-text">{r.qntdAvaliacoes} avaliações</span>
                  </div>
                  <div className="flex gap-1 mt-1 flex-wrap">
                    {r.categorias.slice(0, 3).map((cat) => (
                      <span key={cat.id} className="text-xs px-2 py-0.5 bg-secundary rounded-full text-primary-text">
                        {cat.nome}
                      </span>
                    ))}
                  </div>
                </div>
              </Link>
            ))}
          </div>
        )}
      </div>

      <BottomNav />
    </main>
  );
}
