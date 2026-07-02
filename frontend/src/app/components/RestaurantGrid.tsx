import RestaurantCard from "./RestaurantCard";
import { restaurants } from "../data/restaurant";

export default function RestaurantGrid() {
    return (
        <div className="grid grid-cols-3 gap-5 p-8">
            {restaurants.map((r) => (
                <RestaurantCard key={r.id} id={r.id} name={r.name} image={r.image} rating={Math.round(r.rating)} />
            ))}
        </div>
    );
}