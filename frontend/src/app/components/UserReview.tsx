"use client";
import { useState, useTransition } from "react";
import StarRating from "./StarRating";
import ReviewModal from "./ReviewModal";
import { criarAvaliacao, atualizarAvaliacao, excluirAvaliacao } from "../actions/avaliacoes";

export interface MinhaAvaliacao {
  id: number;
  nota: number;
  comentario: string | null;
  favorito: boolean;
  fotoUrl: string | null;
}

interface UserReviewProps {
  restauranteId: number;
  avaliacao?: MinhaAvaliacao;
}

export default function UserReview({ restauranteId, avaliacao }: UserReviewProps) {
  const [modalOpen, setModalOpen] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [pending, startTransition] = useTransition();

  const revalidate = `/restaurante/${restauranteId}`;

  function handleSubmit(data: { rating: number; comment: string; favorite: boolean; photo: string | null }) {
    setError(null);
    startTransition(async () => {
      const input = {
        restauranteId,
        nota: data.rating,
        comentario: data.comment,
        favorito: data.favorite,
        fotoUrl: data.photo,
      };
      const res = avaliacao
        ? await atualizarAvaliacao(avaliacao.id, input, revalidate)
        : await criarAvaliacao(input, revalidate);
      if (res.error) {
        setError(res.error);
      } else {
        setModalOpen(false);
      }
    });
  }

  function handleDelete() {
    if (!avaliacao) return;
    setError(null);
    startTransition(async () => {
      const res = await excluirAvaliacao(avaliacao.id, revalidate);
      if (res.error) setError(res.error);
    });
  }

  return (
    <div className="mx-4 mt-5 p-4 bg-cards rounded-xl shadow-sm">
      <span className="text-sm font-semibold text-gray-800">Sua avaliação:</span>

      {avaliacao ? (
        <div className="mt-1.5">
          <StarRating rating={avaliacao.nota} />
          <p className="text-sm text-primary-text mt-2 leading-relaxed wrap-break-word">{avaliacao.comentario}</p>
          {avaliacao.fotoUrl && (
            // eslint-disable-next-line @next/next/no-img-element
            <img src={avaliacao.fotoUrl} alt="Foto da avaliação" className="mt-3 w-full h-48 object-cover rounded-xl" />
          )}
          <div className="flex gap-3 mt-3">
            <button
              onClick={() => setModalOpen(true)}
              className="flex-1 py-2 border border-primary text-primary rounded-xl text-sm font-semibold hover:bg-primary/10 transition-colors disabled:opacity-60"
              disabled={pending}
            >
              Editar
            </button>
            <button
              onClick={handleDelete}
              className="flex-1 py-2 border border-red-500 text-red-600 rounded-xl text-sm font-semibold hover:bg-red-50 transition-colors disabled:opacity-60"
              disabled={pending}
            >
              {pending ? "..." : "Excluir"}
            </button>
          </div>
        </div>
      ) : (
        <div className="mt-2">
          <p className="text-sm text-primary-text">Ainda não há avaliação</p>
          <button
            onClick={() => setModalOpen(true)}
            className="w-full mt-3 py-2.5 border border-primary text-primary rounded-xl text-sm font-semibold hover:bg-primary/10 transition-colors"
          >
            Avaliar
          </button>
        </div>
      )}

      {error && <p className="text-xs text-red-600 mt-2">{error}</p>}

      {modalOpen && (
        <ReviewModal
          onClose={() => setModalOpen(false)}
          onSubmit={handleSubmit}
          submitting={pending}
          initialComment={avaliacao?.comentario ?? ""}
          initialRating={avaliacao?.nota ?? 0}
          initialFavorite={avaliacao?.favorito ?? false}
          initialPhoto={avaliacao?.fotoUrl ?? null}
        />
      )}
    </div>
  );
}
