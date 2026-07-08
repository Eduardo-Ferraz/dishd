import RestaurantCard from "./RestaurantCard";
import { fallbackImg } from "../lib/images";
import type { RestauranteDTO } from "../lib/types";

interface RestaurantGridProps {
    restaurantes: RestauranteDTO[];
}

export default function RestaurantGrid({ restaurantes }: RestaurantGridProps) {
    if (restaurantes.length === 0) {
        return <p className="text-sm text-primary-text text-center py-10">Nenhum restaurante ainda.</p>;
    }
    return (
        <div className="grid grid-cols-3 gap-5 p-8">
            {restaurantes.map((r) => (
                <RestaurantCard
                    key={r.id}
                    id={r.id}
                    name={r.nome}
                    image={fallbackImg(r.fotoUrl, r.id)}
                    rating={Math.round(r.notaMedia)}
                />
            ))}
        </div>
    );
}
