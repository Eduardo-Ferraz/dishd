import { notFound } from "next/navigation";
import { getRestaurantById } from "../../data/restaurant";
import DetailHeader from "../../components/DetailHeader";
import RestaurantInfo from "../../components/RestaurantInfo";
import UserReview from "../../components/UserReview";
import Categories from "../../components/Categories";
import ReviewsList from "../../components/ReviewsList";

interface PageProps {
  params: Promise<{ id: string }>;
}

export default async function RestaurantDetailPage({ params }: PageProps) {
  const { id } = await params;
  const restaurant = getRestaurantById(id);

  if (!restaurant) {
    notFound();
  }

  return (
    <main className="min-h-screen bg-background max-w-md mx-auto">
      <DetailHeader image={restaurant.image} name={restaurant.name} />
      <RestaurantInfo
        name={restaurant.name}
        neighborhood={restaurant.neighborhood}
        city={restaurant.city}
        rating={restaurant.rating}
        totalReviews={restaurant.totalReviews}
      />
      <UserReview
        initialRating={restaurant.userReview?.rating}
        initialComment={restaurant.userReview?.comment}
      />
      <Categories categories={restaurant.categories} />
      <ReviewsList reviews={restaurant.reviews} />
    </main>
  );
}