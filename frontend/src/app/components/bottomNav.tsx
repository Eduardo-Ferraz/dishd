"use client";
import { usePathname } from "next/navigation";
import Image from "next/image";
import Link from "next/link";

const navItems = [
    { href: "/minhas-avaliacoes", icon: "/soup-kitchen.svg", label: "Minhas avaliações" },
    { href: "/", icon: "/ion_restaurant.svg", label: "Restaurantes" },
    { href: "/busca", icon: "/search-bold.svg", label: "Buscar" },
]

export default function BottomNav() {
    const pathname = usePathname();

     return (
        <nav className="fixed bottom-0 left-0 right-0 flex items-center justify-around bg-cards border-t border-secundary py-3 px-6">
        {navItems.map((item) => {
            const isActive = pathname === item.href;
            return (
            <Link key={item.href} href={item.href} className="flex flex-col items-center">
                <Image
                src={item.icon}
                alt={item.label}
                width={28}
                height={28}
                className={`transition-opacity ${isActive ? "opacity-100 filter-[invert(48%)_sepia(79%)_saturate(2476%)_hue-rotate(346deg)_brightness(98%)_contrast(92%)]" : "opacity-100"}`}
                />
            </Link>
            );
        })}
        </nav>
    );
}