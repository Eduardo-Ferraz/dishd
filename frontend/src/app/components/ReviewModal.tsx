"use client";
import { useState } from "react";
import StarRating from "./starRating";
import { Videos } from "next/dist/lib/metadata/types/metadata-types";

interface ReviewModalProps {
    onClose: () => void;
    onSubmit: (review: {rating: number; comment: string; favorite: boolean}) => void;
}

const MAX_CHARS = 250;

export default function ReviewModal({ onClose, onSubmit}: ReviewModalProps) {
    const [comment, setComment] = useState("");
    const [rating, setRating] = useState(0);
    const [hovered, setHovered] = useState(0);
    const [favorite, setFavorite] = useState(false);
    
    const canSubmit = comment.length > 0 && rating > 0;

    function handleSubmit() {
        if(!canSubmit) return;
        onSubmit({ rating, comment, favorite });
        onClose();
    }

    return (
        <div className="fixed inset-0 z-50 flex items-end">
            {/* Backdrop */}
            <div className="absolute inset-0 bg-black/40" onClick={onClose} />
        
            {/* Modal */}
            <div className="relative w-full bg-cards rounded-t-3xl px-5 pt-6 pb-10 shadow-xl max-w-[100vw]">
                {/* Header */}
                <div className="flex items-center justify-between mb-5">
                <span className="text-lg font-bold text-gray-800">Avaliação</span>
                <button
                    onClick={onClose}
                    className="w-9 h-9 flex items-center justify-center rounded-xl bg-background text-gray-500 hover:text-gray-800"
                >
                    ✕
                </button>
                </div>
        
                {/* Textarea */}
                <textarea
                value={comment}
                onChange={(e) => {if (e.target.value.length <= MAX_CHARS) setComment(e.target.value);}}
                placeholder="Escreva suas considerações sobre o restaurante"
                className="w-full h-36 border border-primary rounded-xl px-3 py-2.5 text-sm text-gray-700 placeholder-primary-text resize-none focus:outline-none focus:ring-1 focus:ring-primary bg-transparent"
                />
                <p className="text-right text-xs text-primary-text mt-1">
                    {comment.length} / {MAX_CHARS}
                </p>
        
                {/* Anexar foto + Favorito */}
                <div className="flex items-center gap-3 mt-4">
                <button className="flex-1 flex items-center gap-2 border border-primary rounded-xl px-3 py-2.5 text-sm text-primary-text hover:bg-secundary transition-colors">
                    <svg viewBox="0 0 24 24" className="w-5 h-5 text-primary" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z" />
                    <circle cx="12" cy="13" r="4" />
                    </svg>
                    Anexe uma foto
                </button>
        
                <button
                    onClick={() => setFavorite((v) => !v)}
                    className="flex flex-col items-center gap-0.5 px-4"
                >
                    <svg viewBox="0 0 24 24" className={`w-6 h-6 transition-colors ${favorite ? "fill-primary stroke-primary" : "fill-none stroke-primary-text"}`} strokeWidth="2">
                    <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" />
                    </svg>
                    <span className="text-xs text-primary-text">Favorito</span>
                </button>
                </div>
        
                {/* Estrelas interativas */}
                <div className="flex justify-center gap-2 mt-6">
                {[1, 2, 3, 4, 5].map((star) => (
                    <button
                    key={star}
                    onMouseEnter={() => setHovered(star)}
                    onMouseLeave={() => setHovered(0)}
                    onClick={() => setRating(star)}
                    >
                    <svg
                        viewBox="0 0 24 24"
                        className={`w-10 h-10 transition-colors ${
                        star <= (hovered || rating) ? "fill-primary" : "fill-secundary"
                        }`}
                    >
                        <path d="M12 2l2.9 6.6 7.1.6-5.4 4.7 1.6 7-6.2-3.7-6.2 3.7 1.6-7L2 9.2l7.1-.6z" />
                    </svg>
                    </button>
                ))}
                </div>
        
                {/* Botão publicar */}
                <button
                    onClick={handleSubmit}
                    disabled={!canSubmit}
                    className={`w-full mt-6 py-3 rounded-xl font-semibold text-sm transition-colors ${
                        canSubmit
                        ? "bg-primary text-white hover:bg-primary/90"
                        : "bg-secundary text-primary-text cursor-not-allowed"
                    }`}
                    >
                    Publicar avaliação
                </button>
            </div>
        </div>
    );
}