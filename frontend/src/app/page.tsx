import Navbar from "./components/navbar";
import Tabs from "./components/abas";
import RestaurantGrid from "./components/restaurantGrid";
import BottomNav from "./components/bottomNav";

export default function Home() {
  return (
    <main>
      <Navbar></Navbar>
      <Tabs></Tabs>
      <RestaurantGrid></RestaurantGrid>
      <BottomNav></BottomNav>
    </main>
  );
}