import Image from "next/image";
import StarRating from "./StarRating";
import Link from "next/link";
import ReactionButtons from "./ReactionButtons";
import { fallbackImg } from "../lib/images";
import type { AvaliacaoDTO } from "../lib/types";

interface AvaliacoesTabProps {
    avaliacoes: AvaliacaoDTO[];
}

export default function AvaliacoesTab({ avaliacoes }: AvaliacoesTabProps) {
    if (avaliacoes.length === 0) {
        return <p className="text-sm text-primary-text text-center py-10">Nenhuma avaliação ainda.</p>;
    }

    return (
        <div className="flex flex-col divide-y divide-secundary px-4 pb-24">
            {avaliacoes.map((a) => (
                <div key={a.id} className="flex gap-3 py-4">
                    {/* Imagem do restaurante */}
                    <Link href={`/restaurante/${a.restauranteId}`} className="relative w-20 h-20 shrink-0 rounded-xl overflow-hidden">
                        <Image src={fallbackImg(a.restauranteFotoUrl, a.restauranteId)} alt={a.restauranteNome} fill className="object-cover" />
                    </Link>

                    {/* Conteúdo */}
                    <div className="flex-1 min-w-0">
                        <span className="text-sm font-semibold text-gray-800">@{a.usuarioUsername}</span>
                        <p className="text-xs text-primary-text mt-0.5 line-clamp-2">{a.comentario}</p>
                        <Link href={`/restaurante/${a.restauranteId}`} className="text-xs font-medium text-primary mt-1 block hover:underline">{a.restauranteNome}</Link>

                        <div className="flex items-center justify-between mt-1">
                            <StarRating rating={a.nota} />
                            <ReactionButtons
                                avaliacaoId={a.id}
                                likes={a.likes}
                                dislikes={a.dislikes}
                                minhaReacao={a.minhaReacao}
                            />
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
}
