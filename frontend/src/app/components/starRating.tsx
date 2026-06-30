interface StarRatingProps {
    rating: number;
}

export default function StarRating({ rating }: StarRatingProps) {
    return (
        <div className="flex gap-0.5">
            {Array.from({ length: 5 }).map((_, i) => (
                <svg
                key={i}
                viewBox="0 0 24 24"
                className={`w-3.5 h-3.5 ${i < rating ? "fill-primary" : "fill-secundary"}`}
                >
                <path d="M12 2l2.9 6.6 7.1.6-5.4 4.7 1.6 7-6.2-3.7-6.2 3.7 1.6-7L2 9.2l7.1-.6z" />
                </svg>
            ))}
        </div>
    );
}