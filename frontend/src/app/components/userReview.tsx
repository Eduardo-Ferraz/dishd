import StarRating from "./starRating";
 
interface UserReviewProps {
  rating: number;
  comment: string;
}
 
export default function UserReview({ rating, comment }: UserReviewProps) {
  return (
    <div className="mx-4 mt-5 p-4 bg-cards rounded-xl shadow-sm">
      <span className="text-sm font-semibold text-gray-800">Sua avaliação:</span>
      <div className="mt-1.5">
        <StarRating rating={rating} />
      </div>
      <p className="text-sm text-primary-text mt-2 leading-relaxed">{comment}</p>
    </div>
  );
}