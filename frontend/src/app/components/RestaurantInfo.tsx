import StarRating from "./StarRating";

interface RestaurantInfoProps {
    name: string;
    endereco: string | null;
    rating: number;
    totalReviews: number;
}

export default function RestaurantInfo({
    name, endereco, rating, totalReviews,
}: RestaurantInfoProps) {
    return (
       <div className="px-4 pt-2">
            <h1 className="text-2xl font-bold text-gray-800">{name}</h1>
        
            <div className="flex items-center gap-1 mt-1 text-primary-text text-sm">
                <svg viewBox="0 0 24 24" className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
                <circle cx="12" cy="10" r="3" />
                </svg>
                <span>{endereco ?? "Endereço não informado"}</span>
            </div>
        
            <div className="flex items-center gap-3 mt-4 bg-cards rounded-xl px-4 py-3 shadow-sm w-fit">
                <span className="text-2xl font-bold text-gray-800">{rating.toFixed(1)}</span>
                <div className="flex flex-col">
                <StarRating rating={Math.round(rating)} />
                <span className="text-xs text-primary-text mt-0.5">
                    {totalReviews} avaliações gerais
                </span>
                </div>
            </div>
        </div> 
    );
}