export interface Review {
    id: string;
    username: string;
    avatar: string;
    rating: number;
    comment: string;
    likes: number;
    dislikes: number;
}

export interface Restaurant {
    id: string;
    name: string;
    neighborhood: string;
    city: string;
    image: string;
    rating: number;
    totalReviews: number;
    categories: string[];
    userReview?: {
        rating: number;
        comment: string;
    };
    reviews: Review[];
}

export const restaurants: Restaurant[] = [
  {
    id: "mahai",
    name: "Mahai",
    neighborhood: "Bairro República",
    city: "Vitória - ES",
    image: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800&h=500&fit=crop",
    rating: 4.0,
    totalReviews: 128,
    categories: ["Carnes", "Carnes", "Carnes", "Carnes"],
    userReview: {
      rating: 3,
      comment:
        "Lorem ipsum dolor sit amet consectetur. Aliquam est cursus vulputate massa rutrum. Ultrices ut dignissim consequat tellus. Ac velit id felis donec odio curabitur convallis non ultrices. Id senectus sem sed donec. Ullamcorper non odio ut elit viverra pellentesque. Id lectus proin cras vel pretium.",
    },
    reviews: [
      {
        id: "r1",
        username: "@fulano",
        avatar: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&h=100&fit=crop",
        rating: 5,
        comment:
          "Lorem ipsum dolor sit amet consectetur. At sodales malesuada non augue. Turpis nibh blandit morbi sapien lectus. Venenatis a integer turpis cursus penatibus adipiscing cursus. Convallis ac euismod velit in sit scelerisque commodo elit tellus. Rutrum senectus semper id quis integer aliquam. Amet cras interdum facilisi ut.",
        likes: 12,
        dislikes: 1,
      },
      {
        id: "r2",
        username: "@fulano",
        avatar: "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=100&h=100&fit=crop",
        rating: 4,
        comment:
          "Lorem ipsum dolor sit amet consectetur. At sodales malesuada non augue. Turpis nibh blandit morbi sapien lectus. Venenatis a integer turpis cursus penatibus adipiscing cursus. Convallis ac euismod velit in sit scelerisque commodo elit tellus. Rutrum senectus semper id quis integer aliquam. Amet cras interdum facilisi ut.",
        likes: 8,
        dislikes: 0,
      },
    ],
  },
  {
    id: "outback",
    name: "Outback",
    neighborhood: "Praia do Canto",
    city: "Vitória - ES",
    image: "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&h=500&fit=crop",
    rating: 5.0,
    totalReviews: 86,
    categories: ["Carnes", "Steakhouse", "Drinks"],
    reviews: [],
  },
  {
    id: "la-dolina",
    name: "La Dolina",
    neighborhood: "Jardim Camburi",
    city: "Vitória - ES",
    image: "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800&h=500&fit=crop",
    rating: 4.5,
    totalReviews: 54,
    categories: ["Italiana", "Massas"],
    reviews: [],
  },
];
 
export function getRestaurantById(id: string): Restaurant | undefined {
  return restaurants.find((r) => r.id === id);
}