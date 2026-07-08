"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

interface SearchBoxProps {
  initialQuery: string;
}

/** Input controlado que reflete a busca na URL (?q=), com debounce. A pagina (server) refaz o fetch. */
export default function SearchBox({ initialQuery }: SearchBoxProps) {
  const router = useRouter();
  const [query, setQuery] = useState(initialQuery);

  useEffect(() => {
    const t = setTimeout(() => {
      const q = query.trim();
      router.replace(q ? `/busca?q=${encodeURIComponent(q)}` : "/busca");
    }, 300);
    return () => clearTimeout(t);
  }, [query, router]);

  return (
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
  );
}
