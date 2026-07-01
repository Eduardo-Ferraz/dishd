"use client";
import StarRating from "./starRating";
import ReviewModal from "./ReviewModal";
import { useState } from "react";
 
interface UserReviewProps {
  initialRating?: number;
  initialComment?: string;
}
 
export default function UserReview({ initialRating, initialComment }: UserReviewProps) {
  const [review, setReview] = useState(
    initialRating && initialComment
      ? {rating: initialRating, comment: initialComment}
      : null
  );

  const [modalOpen, setModalOpen] = useState(false);

  function handleSubmit(data: {rating: number; comment: string; favorite: boolean})
  {
    setReview({ rating: data.rating, comment: data.comment });
  }

  return (
    <div className="mx-4 mt-5 p-4 bg-cards rounded-xl shadow-sm">
      <span className="text-sm font-semibold text-gray-800">Sua avaliação:</span>

      {review ? (
        <div className="mt-1.5">
          <StarRating rating={review.rating} />
          <p className="text-sm text-primary-text mt-2 leading-relaxed wrap-break-word">{review.comment}</p>
        </div>
      ) : (
        <div className="mt-2">
          <p className="text-sm text-primary-text">Ainda não há avaliação</p>
          <button onClick={() => setModalOpen(true)} className="w-full mt-3 py-2.5 border border-primary text-primary rounded-xl text-sm font-semibold hover:bg-primary/10 transition-colors">
            Avaliar
          </button>
        </div>
      )}
      {modalOpen && (
        <ReviewModal onClose={() => setModalOpen(false)} onSubmit={handleSubmit}/>
      )}
    </div>
  );
}