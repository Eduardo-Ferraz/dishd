"use client";
import { useOptimistic, useTransition } from "react";
import { usePathname } from "next/navigation";
import { definirReacao } from "../actions/avaliacoes";
import type { Reacao } from "../lib/types";

interface ReactionButtonsProps {
  avaliacaoId: number;
  likes: number;
  dislikes: number;
  minhaReacao: Reacao | null;
  /** true => rotulos "gostaram/nao gostaram" (detalhe); false => so numero (feed). */
  labeled?: boolean;
}

export default function ReactionButtons({ avaliacaoId, likes, dislikes, minhaReacao, labeled = false }: ReactionButtonsProps) {
  const pathname = usePathname();
  const [, startTransition] = useTransition();
  const [reacao, setReacao] = useOptimistic<Reacao | null>(minhaReacao);

  // Contagem do backend inclui minhaReacao; isola a base e reaplica a reacao otimista.
  const baseLikes = likes - (minhaReacao === "LIKE" ? 1 : 0);
  const baseDislikes = dislikes - (minhaReacao === "DISLIKE" ? 1 : 0);
  const likesN = baseLikes + (reacao === "LIKE" ? 1 : 0);
  const dislikesN = baseDislikes + (reacao === "DISLIKE" ? 1 : 0);

  function toggle(tipo: Reacao) {
    const next = reacao === tipo ? null : tipo;
    startTransition(async () => {
      setReacao(next);
      await definirReacao(avaliacaoId, next, pathname);
    });
  }

  return (
    <div className={`flex items-center ${labeled ? "gap-4" : "gap-3"} text-xs text-primary-text`}>
      <button
        onClick={() => toggle("LIKE")}
        className={`flex items-center gap-1 transition-colors ${reacao === "LIKE" ? "text-primary font-semibold" : "hover:text-primary"}`}
      >
        <svg viewBox="0 0 24 24" className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2">
          <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3z" />
          <path d="M7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3" />
        </svg>
        {likesN}{labeled ? " gostaram" : ""}
      </button>
      <button
        onClick={() => toggle("DISLIKE")}
        className={`flex items-center gap-1 transition-colors ${reacao === "DISLIKE" ? "text-primary font-semibold" : "hover:text-primary"}`}
      >
        <svg viewBox="0 0 24 24" className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2">
          <path d="M10 15v4a3 3 0 0 0 3 3l4-9V2H5.72a2 2 0 0 0-2 1.7l-1.38 9a2 2 0 0 0 2 2.3z" />
          <path d="M17 2h3a2 2 0 0 1 2 2v7a2 2 0 0 1-2 2h-3" />
        </svg>
        {dislikesN}{labeled ? " não gostaram" : ""}
      </button>
    </div>
  );
}
