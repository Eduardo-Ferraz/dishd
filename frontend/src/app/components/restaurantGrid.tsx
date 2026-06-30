import RestaurantCard from "./restaurantCard";

const restaurants = [
    { name: "Mahai",    image: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=300&h=300&fit=crop", rating: 3 },
    { name: "Outback",  image: "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=300&h=300&fit=crop", rating: 5 },
    { name: "La Dolina",image: "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=300&h=300&fit=crop", rating: 4 },
    { name: "Clericot", image: "https://images.unsplash.com/photo-1552566626-52f8b828add9?w=300&h=300&fit=crop", rating: 1 },
    { name: "Nonna",    image: "https://images.unsplash.com/photo-1537047902294-62a40c20a6ae?w=300&h=300&fit=crop", rating: 2 },
    { name: "By Rock",  image: "https://images.unsplash.com/photo-1424847651672-bf20a4b0982b?w=300&h=300&fit=crop", rating: 4 },
    { name: "Mahai",    image: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=300&h=300&fit=crop", rating: 3 },
    { name: "Outback",  image: "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=300&h=300&fit=crop", rating: 3 },
    { name: "La Dolina",image: "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=300&h=300&fit=crop", rating: 3 },
    { name: "Clericot", image: "https://images.unsplash.com/photo-1552566626-52f8b828add9?w=300&h=300&fit=crop", rating: 3 },
    { name: "Nonna",    image: "https://images.unsplash.com/photo-1537047902294-62a40c20a6ae?w=300&h=300&fit=crop", rating: 3 },
    { name: "By Rock",  image: "https://images.unsplash.com/photo-1424847651672-bf20a4b0982b?w=300&h=300&fit=crop", rating: 3 },
];

export default function RestaurantGrid() {
    return (
        <div className="grid grid-cols-3 gap-5 p-8">
            {restaurants.map((r, i) => (
                <RestaurantCard key={i} name={r.name} image={r.image} rating={r.rating} />
            ))}
        </div>
    );
}