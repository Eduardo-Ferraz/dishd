import StarRating from "./StarRating";
import Image from 'next/image';
import ReactionButtons from "./ReactionButtons";
import { fallbackImg } from "../lib/images";
import type { AvaliacaoDTO } from "../lib/types";

interface ReviewsListProps {
  avaliacoes: AvaliacaoDTO[];
}

export default function ReviewsList({ avaliacoes }: ReviewsListProps) {
  return (
    <div className="px-4 mt-6 pb-24">
      <div className="flex items-center gap-1.5 text-gray-800 font-semibold text-sm mb-3">
        <svg viewBox="0 0 24 24" className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2">
          <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
        </svg>
        Avaliações
      </div>

      {avaliacoes.length === 0 && (
        <p className="text-sm text-primary-text">Nenhuma avaliação ainda.</p>
      )}

      <div className="flex flex-col divide-y divide-secundary">
        {avaliacoes.map((review) => (
          <div key={review.id} className="py-4">
            <div className="flex items-center gap-2">
              <Image
                src={fallbackImg(null, `user-${review.usuarioId}`)}
                alt={review.usuarioUsername}
                width={36}
                height={36}
                className="w-9 h-9 rounded-full object-cover"
              />
              <div>
                <span className="text-sm font-medium text-gray-800">@{review.usuarioUsername}</span>
                <StarRating rating={review.nota} />
              </div>
            </div>

            <p className="text-sm text-primary-text mt-2 leading-relaxed">{review.comentario}</p>

            <div className="mt-2">
              <ReactionButtons
                avaliacaoId={review.id}
                likes={review.likes}
                dislikes={review.dislikes}
                minhaReacao={review.minhaReacao}
                labeled
              />
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
