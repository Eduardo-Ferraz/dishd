import Image from "next/image";
 
const listas = [
  {
    id: "1",
    categoria: "Doces",
    image: "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=200&h=200&fit=crop",
    restaurantes: ["Restaurante 1", "Restaurante 2", "Restaurante 3", "Restaurante 4", "Restaurante 5"],
  },
  {
    id: "2",
    categoria: "Lanches",
    image: "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=200&h=200&fit=crop",
    restaurantes: ["Restaurante 1", "Restaurante 2", "Restaurante 3", "Restaurante 4", "Restaurante 5"],
  },
  {
    id: "3",
    categoria: "Massas",
    image: "https://images.unsplash.com/photo-1552566626-52f8b828add9?w=200&h=200&fit=crop",
    restaurantes: ["Restaurante 1", "Restaurante 2", "Restaurante 3", "Restaurante 4", "Restaurante 5"],
  },
];
 
export default function ListaTab() {
  return (
    <div className="flex flex-col gap-4 px-4 pb-24 pt-2">
      {listas.map((lista) => (
        <div key={lista.id} className="flex gap-3 bg-cards rounded-xl p-3 shadow-sm">
          {/* Imagem */}
          <div className="relative w-24 h-24 shrink-0 rounded-xl overflow-hidden">
            <Image src={lista.image} alt={lista.categoria} fill className="object-cover" />
          </div>
 
          {/* Conteúdo */}
          <div className="flex-1">
            <span className="text-sm font-bold text-gray-800">
              Top 5 avaliações em {lista.categoria}:
            </span>
            <ol className="mt-1.5 flex flex-col gap-0.5">
              {lista.restaurantes.map((r, i) => (
                <li key={i} className="text-xs text-primary-text">
                  {i + 1}– {r}
                </li>
              ))}
            </ol>
          </div>
        </div>
      ))}
    </div>
  );
}