"use client";
import { useState } from "react";
import Image from "next/image";
import Link from "next/link";
import StarRating from "./StarRating";
import { fallbackImg } from "../lib/images";
import type { AvaliacaoDTO } from "../lib/types";

interface MinhasAvaliacoesListProps {
  avaliacoes: AvaliacaoDTO[];
}

export default function MinhasAvaliacoesList({ avaliacoes }: MinhasAvaliacoesListProps) {
  const [query, setQuery] = useState("");
  const [searching, setSearching] = useState(false);

  const filtered = query.trim()
    ? avaliacoes.filter((a) => a.restauranteNome.toLowerCase().includes(query.toLowerCase()))
    : avaliacoes;

  return (
    <div className="px-4 pt-4 pb-24">
      {/* Filtro */}
      {searching ? (
        <div className="flex items-center gap-2 bg-cards rounded-xl px-3 py-2.5 shadow-sm mb-4">
          <svg viewBox="0 0 24 24" className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2">
            <circle cx="11" cy="11" r="8" />
            <path d="M21 21l-4.35-4.35" />
          </svg>
          <input
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Buscar restaurante..."
            className="flex-1 bg-transparent text-sm text-gray-700 placeholder-primary-text focus:outline-none"
            autoFocus
          />
          <button
            onClick={() => { setSearching(false); setQuery(""); }}
            className="text-primary-text hover:text-gray-700 text-xs"
          >Cancelar</button>
        </div>
      ) : (
        <div className="flex items-center gap-2 mb-4">
          <div className="flex-1 bg-cards rounded-xl px-3 py-2.5 shadow-sm">
            <p className="text-xs text-primary-text">Todas</p>
            <p className="text-xs text-primary-text">{filtered.length} avaliações encontradas</p>
          </div>
          <button
            onClick={() => setSearching(true)}
            className="w-11 h-11 flex items-center justify-center bg-cards rounded-xl shadow-sm"
          >
            <svg viewBox="0 0 24 24" className="w-5 h-5" fill="none" stroke="#717171" strokeWidth="2">
              <circle cx="11" cy="11" r="8" />
              <path d="M21 21l-4.35-4.35" />
            </svg>
          </button>
        </div>
      )}

      {/* Grid */}
      <div className="grid grid-cols-3 gap-3 mt-4">
        {filtered.map((a) => (
          <Link
            key={a.id}
            href={`/restaurante/${a.restauranteId}`}
            className="flex flex-col items-center gap-1"
          >
            <div className="relative w-full aspect-square rounded-2xl overflow-hidden bg-secundary">
              <Image
                src={fallbackImg(a.restauranteFotoUrl, a.restauranteId)}
                alt={a.restauranteNome}
                fill
                className="object-cover hover:scale-105 transition-transform duration-300"
              />
            </div>
            <span className="text-xs text-gray-700 font-medium">{a.restauranteNome}</span>
            <StarRating rating={a.nota} />
          </Link>
        ))}
      </div>

      {filtered.length === 0 && (
        <p className="text-sm text-primary-text text-center mt-10">Nenhuma avaliação encontrada.</p>
      )}
    </div>
  );
}
