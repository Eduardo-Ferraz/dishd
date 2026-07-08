import Navbar from "../components/Navbar";
import Tabs from "../components/Abas";
import BottomNav from "../components/BottomNav";
import { api } from "../lib/api";
import type { AvaliacaoDTO, ListaTop5, PagedResponse, RestauranteDTO } from "../lib/types";

/** Agrupa restaurantes por categoria e monta o Top 5 (por notaMedia). Nao ha entidade Lista. */
function buildListas(restaurantes: RestauranteDTO[]): ListaTop5[] {
  const porCategoria = new Map<string, ListaTop5>();
  for (const r of restaurantes) {
    for (const cat of r.categorias) {
      let lista = porCategoria.get(cat.nome);
      if (!lista) {
        lista = { categoria: cat.nome, fotoUrl: r.fotoUrl, restaurantes: [] };
        porCategoria.set(cat.nome, lista);
      }
      lista.restaurantes.push({ id: r.id, nome: r.nome, notaMedia: r.notaMedia });
    }
  }
  return Array.from(porCategoria.values())
    .map((l) => ({
      ...l,
      restaurantes: [...l.restaurantes].sort((a, b) => b.notaMedia - a.notaMedia).slice(0, 5),
    }))
    .sort((a, b) => b.restaurantes.length - a.restaurantes.length);
}

export default async function Home() {
  const [restResp, feedResp] = await Promise.all([
    api<PagedResponse<RestauranteDTO>>("/api/restaurantes?size=30"),
    api<PagedResponse<AvaliacaoDTO>>("/api/avaliacoes?size=20"),
  ]);

  const restaurantes = restResp.content;
  const avaliacoes = feedResp.content;
  const listas = buildListas(restaurantes);

  return (
    <main className="min-h-screen fex flex-col max-w-md mx-auto relative">
      <Navbar />
      <Tabs restaurantes={restaurantes} avaliacoes={avaliacoes} listas={listas} />
      <BottomNav />
    </main>
  );
}
