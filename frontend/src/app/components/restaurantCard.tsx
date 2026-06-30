import Image from 'next/image';

interface RestaurantCardProps {
    name: string;
    image: string;
}

export default function RestaurantCard({name, image}: RestaurantCardProps) {
    return (
        <div className="flex flex-col items-center gap-1 cursor-pointer">
            <div className="relative w-full aspect-square rounded-2xl overflow-hidden bg-secundary hover:scale-105 transition-transform duration-300">
                <Image
                src={image}
                alt={name}
                fill
                />
            </div>
            <span className="text-xs text-gray-700 font-medium">{name}</span>
        </div>
    );
}