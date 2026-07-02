import Navbar from "../components/Navbar";
import Tabs from "../components/Abas";
import BottomNav from "../components/BottomNav";

export default function Home() {
  return (
    <main className="min-h-screen fex flex-col max-w-md mx-auto relative">
      <Navbar />
      <Tabs />
      <BottomNav />
    </main>
  );
}