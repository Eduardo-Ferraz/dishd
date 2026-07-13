import { notFound } from "next/navigation";
import { api, ApiError } from "../../lib/api";
import { getCurrentUser } from "../../lib/session";
import { fallbackImg } from "../../lib/images";
import type { AvaliacaoDTO, PagedResponse, RestauranteDTO } from "../../lib/types";
import DetailHeader from "../../components/DetailHeader";
import RestaurantInfo from "../../components/RestaurantInfo";
import UserReview from "../../components/UserReview";
import Categories from "../../components/Categories";
import ReviewsList from "../../components/ReviewsList";

interface PageProps {
  params: Promise<{ id: string }>;
}

export default async function RestaurantDetailPage({ params }: PageProps) {
  const { id } = await params;

  let restaurante: RestauranteDTO;
  try {
    restaurante = await api<RestauranteDTO>(`/api/restaurantes/${id}`);
  } catch (e) {
    if (e instanceof ApiError && e.status === 404) {
      notFound();
    }
    throw e;
  }

  const [avaliacoesResp, user] = await Promise.all([
    api<PagedResponse<AvaliacaoDTO>>(`/api/restaurantes/${id}/avaliacoes?size=50`),
    getCurrentUser(),
  ]);

  const avaliacoes = avaliacoesResp.content;
  const minha = user ? avaliacoes.find((a) => a.usuarioId === user.id) : undefined;
  const outras = minha ? avaliacoes.filter((a) => a.id !== minha.id) : avaliacoes;

  return (
    <main className="min-h-screen bg-background max-w-md mx-auto">
      <DetailHeader image={fallbackImg(restaurante.fotoUrl, restaurante.id)} name={restaurante.nome} />
      <RestaurantInfo
        name={restaurante.nome}
        endereco={restaurante.endereco}
        rating={restaurante.notaMedia}
        totalReviews={restaurante.qntdAvaliacoes}
      />
      <UserReview
        restauranteId={restaurante.id}
        avaliacao={
          minha
            ? { id: minha.id, nota: minha.nota, comentario: minha.comentario, favorito: minha.favorito, fotoUrl: minha.fotoUrl }
            : undefined
        }
      />
      <Categories categories={restaurante.categorias.map((c) => c.nome)} />
      <ReviewsList avaliacoes={outras} />
    </main>
  );
}
