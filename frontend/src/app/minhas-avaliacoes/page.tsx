import DetailHeader from "../components/detailHeader";
import { restaurants } from "@/src/app/data/restaurant";

export default function MinhasAvaliacoes() {
  return (
    <div className="flex flex-col gap-6">
      {restaurants.map((restaurante) => (
        <DetailHeader 
          key={restaurante.id} // Sempre adicione a key ao usar .map!
          name={restaurante.name} 
          image={restaurante.image} 
        />
      ))}
    </div>
  );
}