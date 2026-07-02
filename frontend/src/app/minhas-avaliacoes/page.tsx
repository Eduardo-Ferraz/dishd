"use client";
import { useState } from "react";
import Image from "next/image";
import Link from "next/link";
import Navbar from "../components/Navbar";
import BottomNav from "../components/BottomNav";
import StarRating from "../components/StarRating";
import Tabs from "../components/Abas";

const minhasAvaliacoes = [
  { id: "mahai",     name: "Mahai",     image: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=300&h=300&fit=crop", rating: 3 },
  { id: "outback",   name: "Outback",   image: "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=300&h=300&fit=crop", rating: 5 },
  { id: "la-dolina", name: "La Dolina", image: "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=300&h=300&fit=crop", rating: 4 },
  { id: "mahai",     name: "Mahai",     image: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=300&h=300&fit=crop", rating: 3 },
  { id: "outback",   name: "Outback",   image: "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=300&h=300&fit=crop", rating: 5 },
  { id: "la-dolina", name: "La Dolina", image: "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=300&h=300&fit=crop", rating: 4 },
  { id: "mahai",     name: "Mahai",     image: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=300&h=300&fit=crop", rating: 3 },
  { id: "outback",   name: "Outback",   image: "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=300&h=300&fit=crop", rating: 5 },
  { id: "la-dolina", name: "La Dolina", image: "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=300&h=300&fit=crop", rating: 4 },
];

export default function MinhasAvaliacoesPage() {
  const [query, setQuery] = useState("");
  const [searching, setSearching] = useState(false);

  const filtered = query.trim()
    ? minhasAvaliacoes.filter((r) =>
      r.name.toLowerCase().includes(query.toLowerCase())
  ) : minhasAvaliacoes;

  return (
    <main className="min-h-screen bg-background flex flex-col relative">
      <Navbar />
 
      {/* Header da aba */}
      <Tabs />
 
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
              onClick={() => {setSearching(false); setQuery("");}}
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
          {filtered.map((r, i) => (
            <Link
              key={i}
              href={`/restaurante/${r.id}`}
              className="flex flex-col items-center gap-1"
            >
              <div className="relative w-full aspect-square rounded-2xl overflow-hidden bg-secundary">
                <Image
                  src={r.image}
                  alt={r.name}
                  fill
                  className="object-cover hover:scale-105 transition-transform duration-300"
                />
              </div>
              <span className="text-xs text-gray-700 font-medium">{r.name}</span>
              <StarRating rating={r.rating} />
            </Link>
          ))}
        </div>
 
        {filtered.length === 0 && (
          <p className="text-sm text-primary-text text-center mt-10">Nenhuma avaliação encontrada.</p>
        )}
      </div>
 
      <BottomNav />
    </main>
  );
}