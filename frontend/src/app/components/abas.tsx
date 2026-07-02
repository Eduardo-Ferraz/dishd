"use client";
import { usePathname } from "next/navigation";
import { useState } from "react"
import RestaurantGrid from "./restaurantGrid";
import AvaliacoesTab from "./AvaliacoesTab";
import ListaTab from "./ListaTab";

const tabs = ["Restaurantes", "Avaliações", "Lista"];

export default function Tabs() {
    const [active, setActive] = useState(0);
    const pathname = usePathname();

    if (pathname !== "/") {
        return (
        <nav className="border-b border-secundary bg-background px-4 py-3">
            <span className="text-base font-semibold text-gray-800">Avaliações</span>
        </nav>
        );
    }

    return (
        <>
            <nav className="flex bg-cards border-b border-secundary">
                {tabs.map((tab, i) => (
                    <button
                    key={tab}
                    onClick={() => setActive(i)}
                    className={`flex-1 py-1 text-sm font-work font-medium font-xs transition-colors ${
                        active === i
                        ? "text-primary border-b-2 border-primary"
                        : "text-primary-text"
                    }`}
                    >
                    {tab}
                    </button>
                ))}
            </nav>
            {active === 0 && <RestaurantGrid />}
            {active === 1 && <AvaliacoesTab />}
            {active === 2 && <ListaTab />}
        </>
    )
}