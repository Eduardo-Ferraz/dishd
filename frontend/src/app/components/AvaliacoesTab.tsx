"use client";
import Image from "next/image";
import StarRating from "./StarRating";
import Link from "next/link";
import { useState } from "react";

const avaliacoes = [
    {
        id: "1",
        username: "@fulano",
        comment: "Lorem ipsum dolor sit amet consectetur. Sed felis cum ultrices lacus malesuada tortor duis.",
        restaurant: "Mahai",
        restaurantId: "mahai",
        image: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=200&h=200&fit=crop",
        rating: 4,
        likes: 100,
        dislikes: 100,
    },
    {
        id: "2",
        username: "@fulano",
        comment: "Lorem ipsum dolor sit amet consectetur. Sed felis cum ultrices lacus malesuada tortor duis.",
        restaurant: "Mahai",
        restaurantId: "mahai",
        image: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=200&h=200&fit=crop",
        rating: 5,
        likes: 100,
        dislikes: 100,
    },
    {
        id: "3",
        username: "@fulano",
        comment: "Lorem ipsum dolor sit amet consectetur. Sed felis cum ultrices lacus malesuada tortor duis.",
        restaurant: "Mahai",
        restaurantId: "mahai",
        image: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=200&h=200&fit=crop",
        rating: 3,
        likes: 100,
        dislikes: 100,
    },
    {
        id: "4",
        username: "@fulano",
        comment: "Lorem ipsum dolor sit amet consectetur. Sed felis cum ultrices lacus malesuada tortor duis.",
        restaurant: "Mahai",
        restaurantId: "mahai",
        image: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=200&h=200&fit=crop",
        rating: 4,
        likes: 100,
        dislikes: 100,
    },
];

export default function AvaliacoesTab() {
    const [votes, setVotes] = useState<Record<string, "like" | "dislike" | null>>({});

    function handleVote(id: string, type: "like" | "dislike"){
        setVotes((prev) => ({
            ...prev,
            [id]: prev[id] === type ? null : type,
        }));
    }

    return (
        <div className="flex flex-col divide-y divide-secundary px-4 pb-24">
            {avaliacoes.map((a) => {
                const vote = votes[a.id];
                const likes = a.likes + (vote === "like" ? 1 : 0);
                const dislikes = a.dislikes + (vote === "dislike" ? 1 : 0);

                return (
                    <div key={a.id} className="flex gap-3 py-4">
                    {/* Imagem do restaurante */}
                    <Link href={`/restaurante/${a.restaurantId}`} className="relative w-20 h-20 shrink-0 rounded-xl overflow-hidden">
                        <Image src={a.image} alt={a.restaurant} fill className="object-cover" />
                    </Link>
            
                    {/* Conteúdo */}
                    <div className="flex-1 min-w-0">
                        <span className="text-sm font-semibold text-gray-800">{a.username}</span>
                        <p className="text-xs text-primary-text mt-0.5 line-clamp-2">{a.comment}</p>
                        <Link href={`/restaurante/${a.restaurantId}`} className="text-xs font-medium text-primary mt-1 block hover:underline" >{a.restaurant}</Link>

                        <div className="flex items-center justify-between mt-1">
                            <StarRating rating={a.rating} />
                            <div className="flex items-center gap-3 text-xs text-primary-text">
                                <button onClick={() => handleVote(a.id, "like")} className={`flex items-center gap-1 transition-colors ${vote === "like" ? "text-primary font-semibold" : "hover:text-primary"}`}>
                                    <svg viewBox="0 0 24 24" className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2">
                                        <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3z" />
                                        <path d="M7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3" />
                                    </svg>
                                    {likes}
                                </button>
                                <button onClick={() => handleVote(a.id, "dislike")} className={`flex items-center gap-1 transition-colors ${vote === "dislike" ? "text-primary font-semibold" : "hover:text-primary"}`}>
                                    <svg viewBox="0 0 24 24" className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2">
                                        <path d="M10 15v4a3 3 0 0 0 3 3l4-9V2H5.72a2 2 0 0 0-2 1.7l-1.38 9a2 2 0 0 0 2 2.3z" />
                                        <path d="M17 2h3a2 2 0 0 1 2 2v7a2 2 0 0 1-2 2h-3" />
                                    </svg>
                                    {dislikes}
                                </button>
                            </div>
                        </div>
                    </div>
                    </div>
                )
            })}
        </div>
    );
}