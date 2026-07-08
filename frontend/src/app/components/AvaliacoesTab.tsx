"use client";
import Image from "next/image";
import StarRating from "./StarRating";
import Link from "next/link";
import { useState } from "react";
import { fallbackImg } from "../lib/images";
import type { AvaliacaoDTO, Reacao } from "../lib/types";

interface AvaliacoesTabProps {
    avaliacoes: AvaliacaoDTO[];
}

export default function AvaliacoesTab({ avaliacoes }: AvaliacoesTabProps) {
    // Reacao local (visual). F3 liga em Server Action + revalidatePath.
    const [votes, setVotes] = useState<Record<number, Reacao | null>>(
        () => Object.fromEntries(avaliacoes.map((a) => [a.id, a.minhaReacao]))
    );

    function handleVote(id: number, type: Reacao) {
        setVotes((prev) => ({
            ...prev,
            [id]: prev[id] === type ? null : type,
        }));
    }

    if (avaliacoes.length === 0) {
        return <p className="text-sm text-primary-text text-center py-10">Nenhuma avaliação ainda.</p>;
    }

    return (
        <div className="flex flex-col divide-y divide-secundary px-4 pb-24">
            {avaliacoes.map((a) => {
                const vote = votes[a.id] ?? null;
                // Contagem do backend ja inclui minhaReacao; ajusta pelo delta do toggle local.
                const likes = a.likes - (a.minhaReacao === "LIKE" ? 1 : 0) + (vote === "LIKE" ? 1 : 0);
                const dislikes = a.dislikes - (a.minhaReacao === "DISLIKE" ? 1 : 0) + (vote === "DISLIKE" ? 1 : 0);

                return (
                    <div key={a.id} className="flex gap-3 py-4">
                    {/* Imagem do restaurante */}
                    <Link href={`/restaurante/${a.restauranteId}`} className="relative w-20 h-20 shrink-0 rounded-xl overflow-hidden">
                        <Image src={fallbackImg(a.restauranteFotoUrl, a.restauranteId)} alt={a.restauranteNome} fill className="object-cover" />
                    </Link>

                    {/* Conteúdo */}
                    <div className="flex-1 min-w-0">
                        <span className="text-sm font-semibold text-gray-800">@{a.usuarioUsername}</span>
                        <p className="text-xs text-primary-text mt-0.5 line-clamp-2">{a.comentario}</p>
                        <Link href={`/restaurante/${a.restauranteId}`} className="text-xs font-medium text-primary mt-1 block hover:underline" >{a.restauranteNome}</Link>

                        <div className="flex items-center justify-between mt-1">
                            <StarRating rating={a.nota} />
                            <div className="flex items-center gap-3 text-xs text-primary-text">
                                <button onClick={() => handleVote(a.id, "LIKE")} className={`flex items-center gap-1 transition-colors ${vote === "LIKE" ? "text-primary font-semibold" : "hover:text-primary"}`}>
                                    <svg viewBox="0 0 24 24" className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2">
                                        <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3z" />
                                        <path d="M7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3" />
                                    </svg>
                                    {likes}
                                </button>
                                <button onClick={() => handleVote(a.id, "DISLIKE")} className={`flex items-center gap-1 transition-colors ${vote === "DISLIKE" ? "text-primary font-semibold" : "hover:text-primary"}`}>
                                    <svg viewBox="0 0 24 24" className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2">
                                        <path d="M10 15v4a3 3 0 0 0 3 3l4-9V2H5.72a2 2 0 0 0-2 1.7l-1.38 9a2 2 0 0 0 2 2.3z" />
                                        <path d="M17 2h3a2 2 0 0 1 2 2v7a2 2 0 0 1-2 2h-3" />
                                    </svg>
                                    {dislikes}
                                </button>
                            </div>
                        </div>
                    </div>
                    </div>
                )
            })}
        </div>
    );
}
