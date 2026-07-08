"use client";
import { usePathname } from "next/navigation";
import { useState } from "react";
import RestaurantGrid from "./RestaurantGrid";
import AvaliacoesTab from "./AvaliacoesTab";
import ListaTab from "./ListaTab";
import type { AvaliacaoDTO, ListaTop5, RestauranteDTO } from "../lib/types";

const tabs = ["Restaurantes", "Avaliações", "Lista"];

interface TabsProps {
    restaurantes?: RestauranteDTO[];
    avaliacoes?: AvaliacaoDTO[];
    listas?: ListaTop5[];
}

export default function Tabs({ restaurantes = [], avaliacoes = [], listas = [] }: TabsProps) {
    const [active, setActive] = useState(0);
    const pathname = usePathname();

    if (pathname === "/minhas-avaliacoes") {
        return (
        <nav className="flex bg-cards border-b border-secundary">
            <button className="flex-1 py-1 text-sm font-work font-medium font-xs transition-colors text-primary border-b-2 border-primary">
                Minhas Avaliações
            </button>
        </nav>
        );
    }

    if (pathname !== "/home") {
        return null;
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
            {active === 0 && <RestaurantGrid restaurantes={restaurantes} />}
            {active === 1 && <AvaliacoesTab avaliacoes={avaliacoes} />}
            {active === 2 && <ListaTab listas={listas} />}
        </>
    )
}
