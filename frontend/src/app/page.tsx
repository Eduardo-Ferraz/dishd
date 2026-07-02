import Navbar from "./components/navbar";
import Tabs from "./components/abas";
import RestaurantGrid from "./components/restaurantGrid";
import BottomNav from "./components/bottomNav";

export default function Home() {
  return (
    <main className="min-h-screen fex flex-col max-w-md mx-auto relative">
      <Navbar />
      <Tabs />
      <BottomNav />
    </main>
  );
}