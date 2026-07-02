interface CategoriesProps {
  categories: string[];
}
 
export default function Categories({ categories }: CategoriesProps) {
  return (
    <div className="px-4 mt-5">
      <div className="flex items-center gap-1.5 text-gray-800 font-semibold text-sm">
        <svg viewBox="0 0 24 24" className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2">
          <path d="M20.59 13.41L13.42 20.6a2 2 0 0 1-2.83 0L2.83 12.83A2 2 0 0 1 2 11.41V4a2 2 0 0 1 2-2h7.41a2 2 0 0 1 1.41.59l7.76 7.76a2 2 0 0 1 0 2.83z" />
          <circle cx="7.5" cy="7.5" r="1.5" />
        </svg>
        Categorias
      </div>
      <div className="flex flex-wrap gap-2 mt-2.5">
        {categories.map((cat, i) => (
          <span
            key={i}
            className="px-4 py-1.5 text-xs font-medium text-gray-700 bg-cards rounded-full shadow-sm"
          >
            {cat}
          </span>
        ))}
      </div>
    </div>
  );
}