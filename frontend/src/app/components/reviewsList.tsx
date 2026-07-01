"use client"
import StarRating from "./starRating";
import { Review } from "../data/restaurant";
import Image from 'next/image';
import { useState } from "react";
 
interface ReviewsListProps {
  reviews: Review[];
}
 
export default function ReviewsList({ reviews }: ReviewsListProps) {
  const [votes, setVotes] = useState<Record<string, "like" | "dislike" | null>>({});

  function handleVote(id: string, type: "like" | "dislike") {
    setVotes((prev) => ({
      ...prev,
      [id]: prev[id] === type ? null : type, // clicou de novo = desfaz
    }));
  }

  return (
    <div className="px-4 mt-6 pb-24">
      <div className="flex items-center gap-1.5 text-gray-800 font-semibold text-sm mb-3">
        <svg viewBox="0 0 24 24" className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2">
          <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
        </svg>
        Avaliações
      </div>
 
      <div className="flex flex-col divide-y divide-secundary">
        {reviews.map((review) => {
          const vote = votes[review.id];
          const likes = review.likes + (vote === "like" ? 1 : 0);
          const dislikes = review.dislikes + (vote === "dislike" ? 1 : 0);

          return (
            <div key={review.id} className="py-4">
              <div className="flex items-center gap-2">
                <Image
                  src={review.avatar}
                  alt={review.username}
                  width={36}
                  height={36}
                  className="w-9 h-9 rounded-full object-cover"
                />
                <div>
                  <span className="text-sm font-medium text-gray-800">{review.username}</span>
                  <StarRating rating={review.rating} />
                </div>
              </div>

              <p className="text-sm text-primary-text mt-2 leading-relaxed">{review.comment}</p>

              <div className="flex items-center gap-4 mt-2 text-xs text-primary-text">
                <button
                  onClick={() => handleVote(review.id, "like")}
                  className={`flex items-center gap-1 transition-colors ${vote === "like" ? "text-primary font-semibold" : "hover:text-primary"}`}
                >
                  <svg viewBox="0 0 24 24" className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3z" />
                    <path d="M7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3" />
                  </svg>
                  {likes} gostaram
                </button>
                <button
                  onClick={() => handleVote(review.id, "dislike")}
                  className={`flex items-center gap-1 transition-colors ${vote === "dislike" ? "text-primary font-semibold" : "hover:text-primary"}`}
                >
                  <svg viewBox="0 0 24 24" className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M10 15v4a3 3 0 0 0 3 3l4-9V2H5.72a2 2 0 0 0-2 1.7l-1.38 9a2 2 0 0 0 2 2.3z" />
                    <path d="M17 2h3a2 2 0 0 1 2 2v7a2 2 0 0 1-2 2h-3" />
                  </svg>
                  {dislikes} não gostaram
                </button>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}