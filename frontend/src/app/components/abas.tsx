"use client";
import { usePathname } from "next/navigation";
import { useState } from "react"

const tabs = ["Restaurantes", "Avaliações", "Lista"];

export default function Tabs() {
    const [active, setActive] = useState(0);
    const pathname = usePathname();

    if (pathname === "/minhas-avaliacoes") {
        return (
        <nav className="flex bg-cards">
            <span className="flex-1 py-1 text-sm font-work font-medium font-xs text-primary border-b-2 border-primary">Minhas Avaliações</span>
        </nav>
        );
    }

    return (
        <nav className="flex bg-cards">
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
    )
}