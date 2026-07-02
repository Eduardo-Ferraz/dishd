"use client"
import { useState } from "react";
import { useRouter } from "next/navigation";
import Image from 'next/image';

interface DetailHeaderProps {
    image: string;
    name: string;
}

export default function DetailHeader({ image, name }: DetailHeaderProps) {
    const router = useRouter();
    const [menuOpen, setMenuOpen] = useState(false);

    return (
        <div className="relative">
            {/* Imagem de fundo borrada */}
            <div
                className="absolute inset-0 h-56 bg-cover bg-center blur-md scale-110 opacity-60"
                style={{ backgroundImage: `url(${image})` }}
            />
            <div className="absolute inset-0 h-56 bg-background/40" />
        
            {/* Conteúdo */}
            <div className="relative flex items-center justify-between px-4 pt-4">
                <button
                onClick={() => router.back()}
                className="w-10 h-10 flex items-center justify-center rounded-xl bg-cards/90 shadow-sm"
                >
                <svg viewBox="0 0 24 24" className="w-5 h-5" fill="none" stroke="#1f2937" strokeWidth="2">
                    <path d="M19 12H5M12 19l-7-7 7-7" />
                </svg>
                </button>
        
                <div className="relative">
                <button
                    onClick={() => setMenuOpen((v) => !v)}
                    className="w-10 h-10 flex items-center justify-center rounded-xl bg-cards/90 shadow-sm"
                >
                    <svg viewBox="0 0 24 24" className="w-5 h-5" fill="#1f2937">
                    <circle cx="5" cy="12" r="2" />
                    <circle cx="12" cy="12" r="2" />
                    <circle cx="19" cy="12" r="2" />
                    </svg>
                </button>
        
                {menuOpen && (
                    <div className="absolute right-0 mt-2 w-44 bg-cards rounded-xl shadow-lg overflow-hidden z-10">
                    <button className="w-full text-left px-4 py-2.5 text-sm text-gray-700 hover:bg-secundary">
                        Editar
                    </button>
                    <button className="w-full text-left px-4 py-2.5 text-sm text-gray-700 hover:bg-secundary">
                        Compartilhar
                    </button>
                    <button className="w-full text-left px-4 py-2.5 text-sm text-red-600 hover:bg-secundary">
                        Denunciar
                    </button>
                    </div>
                )}
                </div>
            </div>
        
            {/* Foto principal do restaurante */}
            <div className="relative flex justify-center pt-4 pb-2">
                <div className="relative w-44 h-44">
                    <Image 
                    src={image} 
                    alt={name} 
                    fill
                    className="object-cover rounded-2xl shadow-lg"
                    />
                </div>
            </div>
        </div>
    );
}