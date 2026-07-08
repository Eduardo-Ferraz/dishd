import Image from 'next/image';
import StarRating from './StarRating';
import Link from 'next/link';

interface RestaurantCardProps {
    id: number | string;
    name: string;
    image: string;
    rating: number;
}

export default function RestaurantCard({id, name, image, rating}: RestaurantCardProps) {
    return (
        <Link href={`/restaurante/${id}`} className="flex flex-col items-center cursor-pointer">
            <div className='w-full aspect-square rounded-2xl overflow-hidden bg-secundary'>
                <div className="relative w-full aspect-square rounded-2xl overflow-hidden bg-secundary hover:scale-105 transition-transform duration-300">
                    <Image
                    src={image}
                    alt={name}
                    fill
                    />
                </div>
                
            </div>
            <span className="text-xs text-gray-700 font-medium">{name}</span>
            <StarRating rating={rating} />
        </Link>
    );
}