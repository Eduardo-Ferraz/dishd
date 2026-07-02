"use client";
import { useState } from "react";
import Image from "next/image";
import Link from "next/link";
import Navbar from "../components/Navbar";
import BottomNav from "../components/BottomNav";
import { restaurants } from "../data/restaurant";
import StarRating from "../components/StarRating";

export default function BuscaPage() {
  const [query, setQuery] = useState("");

  const results = query.trim().length === 0
    ? restaurants
    : restaurants.filter((r) =>
      r.name.toLowerCase().includes(query.toLowerCase()) || 
      r.categories.some((c) => c.toLowerCase().includes(query.toLowerCase())) ||
      r.neighborhood.toLowerCase().includes(query.toLowerCase())
    );

  return (
    <main className="min-h-screen bg-background flex flex-col relative">
      <Navbar />
 
      <div className="px-4 pt-4 pb-24">
        {/* Input de busca */}
        <div className="flex items-center gap-2 bg-secundary rounded-xl px-3 py-2.5">
          <svg viewBox="0 0 24 24" className="w-4 h-4 text-primary-text shrink-0" fill="none" stroke="currentColor" strokeWidth="2">
            <circle cx="11" cy="11" r="8" />
            <path d="M21 21l-4.35-4.35" />
          </svg>
          <input
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Pesquisar restaurantes, categorias..."
            className="flex-1 bg-transparent text-sm text-gray-700 placeholder-primary-text focus:outline-none"
            autoFocus
          />
          {query && (
            <button onClick={() => setQuery("")} className="text-primary-text hover:text-gray-700">
              ✕
            </button>
          )}
        </div>
 
        {/* Label */}
        <p className="text-xs text-primary-text mt-4 mb-2 font-medium">
          {query.trim() ? `${results.length} resultado(s) para "${query}"` : "Sugestões"}
        </p>
 
        {/* Resultados / Sugestões */}
        {results.length === 0 ? (
          <p className="text-sm text-primary-text text-center mt-10">Nenhum restaurante encontrado.</p>
        ) : (
          <div className="flex flex-col divide-y divide-secundary">
            {results.map((r) => (
              <Link key={r.id} href={`/restaurante/${r.id}`} className="flex gap-3 py-3 hover:opacity-80 transition-opacity">
                <div className="relative w-16 h-16 shrink-0 rounded-xl overflow-hidden">
                  <Image src={r.image} alt={r.name} fill className="object-cover" />
                </div>
                <div className="flex-1 min-w-0">
                  <span className="text-sm font-semibold text-gray-800">{r.name}</span>
                  <p className="text-xs text-primary-text mt-0.5">{r.neighborhood}, {r.city}</p>
                  <div className="flex items-center gap-2 mt-1">
                    <StarRating rating={Math.round(r.rating)} />
                    <span className="text-xs text-primary-text">{r.totalReviews} avaliações</span>
                  </div>
                  <div className="flex gap-1 mt-1 flex-wrap">
                    {r.categories.slice(0, 3).map((cat, i) => (
                      <span key={i} className="text-xs px-2 py-0.5 bg-secundary rounded-full text-primary-text">
                        {cat}
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