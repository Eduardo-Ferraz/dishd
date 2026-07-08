import Image from "next/image";
import { fallbackImg } from "../lib/images";
import type { ListaTop5 } from "../lib/types";

interface ListaTabProps {
  listas: ListaTop5[];
}

export default function ListaTab({ listas }: ListaTabProps) {
  if (listas.length === 0) {
    return <p className="text-sm text-primary-text text-center py-10">Nenhuma lista ainda.</p>;
  }
  return (
    <div className="flex flex-col gap-4 px-4 pb-24 pt-2">
      {listas.map((lista) => (
        <div key={lista.categoria} className="flex gap-3 bg-cards rounded-xl p-3 shadow-sm">
          {/* Imagem */}
          <div className="relative w-24 h-24 shrink-0 rounded-xl overflow-hidden">
            <Image
              src={fallbackImg(lista.fotoUrl, lista.categoria)}
              alt={lista.categoria}
              fill
              className="object-cover"
            />
          </div>

          {/* Conteúdo */}
          <div className="flex-1">
            <span className="text-sm font-bold text-gray-800">
              Top 5 avaliações em {lista.categoria}:
            </span>
            <ol className="mt-1.5 flex flex-col gap-0.5">
              {lista.restaurantes.map((r, i) => (
                <li key={r.id} className="text-xs text-primary-text">
                  {i + 1}– {r.nome}
                </li>
              ))}
            </ol>
          </div>
        </div>
      ))}
    </div>
  );
}
