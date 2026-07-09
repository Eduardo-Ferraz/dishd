"use client";
import { useState } from "react";

interface ReviewModalProps {
    onClose: () => void;
    onSubmit: (review: {rating: number; comment: string; favorite: boolean}) => void;
    initialComment?: string;
    initialRating?: number;
    initialFavorite?: boolean;
    submitting?: boolean;
}

const MAX_CHARS = 250;

export default function ReviewModal({ onClose, onSubmit, initialComment = "", initialRating = 0, initialFavorite = false, submitting = false }: ReviewModalProps) {
    const [comment, setComment] = useState(initialComment);
    const [rating, setRating] = useState(initialRating);
    const [hovered, setHovered] = useState(0);
    const [favorite, setFavorite] = useState(initialFavorite);

    const canSubmit = comment.length > 0 && rating > 0 && !submitting;

    function handleSubmit() {
        if(!canSubmit) return;
        onSubmit({ rating, comment, favorite });
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
                <div className="flex items-center gap-2">
                <button
                    onClick={() => setFavorite((v) => !v)}
                    aria-label={favorite ? "Remover dos favoritos" : "Marcar como favorito"}
                    className="w-9 h-9 flex items-center justify-center rounded-xl bg-background hover:bg-secundary transition-colors"
                >
                    <svg viewBox="0 0 24 24" className={`w-5 h-5 transition-colors ${favorite ? "fill-primary stroke-primary" : "fill-none stroke-primary-text"}`} strokeWidth="2">
                    <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" />
                    </svg>
                </button>
                <button
                    onClick={onClose}
                    className="w-9 h-9 flex items-center justify-center rounded-xl bg-background text-gray-500 hover:text-gray-800"
                >
                    ✕
                </button>
                </div>
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
        
                {/* Estrelas interativas (suporta meia estrela) */}
                <div className="flex justify-center gap-2 mt-6" onMouseLeave={() => setHovered(0)}>
                {[1, 2, 3, 4, 5].map((star) => {
                    const display = hovered || rating;
                    const fill = Math.max(0, Math.min(1, display - (star - 1))); // 0, 0.5 ou 1
                    return (
                    <div key={star} className="relative w-10 h-10">
                        {/* Estrela base (vazia) */}
                        <svg viewBox="0 0 24 24" className="absolute inset-0 w-10 h-10 fill-secundary">
                            <path d="M12 2l2.9 6.6 7.1.6-5.4 4.7 1.6 7-6.2-3.7-6.2 3.7 1.6-7L2 9.2l7.1-.6z" />
                        </svg>
                        {/* Preenchimento parcial */}
                        <div className="absolute inset-0 overflow-hidden" style={{ width: `${fill * 100}%` }}>
                            <svg viewBox="0 0 24 24" className="w-10 h-10 fill-primary">
                                <path d="M12 2l2.9 6.6 7.1.6-5.4 4.7 1.6 7-6.2-3.7-6.2 3.7 1.6-7L2 9.2l7.1-.6z" />
                            </svg>
                        </div>
                        {/* Zona de clique: metade esquerda = x.5, metade direita = x */}
                        <button
                            type="button"
                            aria-label={`${star - 0.5} estrelas`}
                            className="absolute inset-y-0 left-0 w-1/2 z-10"
                            onMouseEnter={() => setHovered(star - 0.5)}
                            onClick={() => setRating(star - 0.5)}
                        />
                        <button
                            type="button"
                            aria-label={`${star} estrelas`}
                            className="absolute inset-y-0 right-0 w-1/2 z-10"
                            onMouseEnter={() => setHovered(star)}
                            onClick={() => setRating(star)}
                        />
                    </div>
                    );
                })}
                </div>
                <p className="text-center text-xs text-primary-text mt-2">{rating > 0 ? `${rating} / 5` : "Toque para avaliar"}</p>
        
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
                    {submitting ? "Publicando..." : "Publicar avaliação"}
                </button>
            </div>
        </div>
    );
}