"use client";
import { useRef, useState } from "react";

interface ReviewModalProps {
    onClose: () => void;
    onSubmit: (review: {rating: number; comment: string; favorite: boolean; photo: string | null}) => void;
    initialComment?: string;
    initialRating?: number;
    initialFavorite?: boolean;
    initialPhoto?: string | null;
    submitting?: boolean;
}

const MAX_CHARS = 250;

// Reduz a imagem (max 900px, JPEG) e devolve data URL base64 pequeno,
// para caber tranquilo no corpo da Server Action e na coluna do banco.
async function comprimirImagem(file: File, max = 900, quality = 0.7): Promise<string> {
    const bitmap = await createImageBitmap(file);
    const escala = Math.min(1, max / Math.max(bitmap.width, bitmap.height));
    const w = Math.round(bitmap.width * escala);
    const h = Math.round(bitmap.height * escala);
    const canvas = document.createElement("canvas");
    canvas.width = w;
    canvas.height = h;
    canvas.getContext("2d")!.drawImage(bitmap, 0, 0, w, h);
    bitmap.close();
    return canvas.toDataURL("image/jpeg", quality);
}

export default function ReviewModal({ onClose, onSubmit, initialComment = "", initialRating = 0, initialFavorite = false, initialPhoto = null, submitting = false }: ReviewModalProps) {
    const [comment, setComment] = useState(initialComment);
    const [rating, setRating] = useState(initialRating);
    const [hovered, setHovered] = useState(0);
    const [favorite, setFavorite] = useState(initialFavorite);
    const [photo, setPhoto] = useState<string | null>(initialPhoto);
    const [photoError, setPhotoError] = useState<string | null>(null);
    const fileInput = useRef<HTMLInputElement>(null);

    const canSubmit = comment.length > 0 && rating > 0 && !submitting;

    async function handlePhoto(e: React.ChangeEvent<HTMLInputElement>) {
        const file = e.target.files?.[0];
        e.target.value = ""; // permite re-selecionar o mesmo arquivo
        if (!file) return;
        setPhotoError(null);
        try {
            setPhoto(await comprimirImagem(file));
        } catch {
            setPhotoError("Não foi possível processar a imagem.");
        }
    }

    function handleSubmit() {
        if(!canSubmit) return;
        onSubmit({ rating, comment, favorite, photo });
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

                {/* Foto (opcional) */}
                <input
                    ref={fileInput}
                    type="file"
                    accept="image/*"
                    className="hidden"
                    onChange={handlePhoto}
                />
                {photo ? (
                    <div className="relative mt-3">
                        {/* eslint-disable-next-line @next/next/no-img-element */}
                        <img src={photo} alt="Foto da avaliação" className="w-full h-44 object-cover rounded-xl" />
                        <button
                            type="button"
                            onClick={() => setPhoto(null)}
                            aria-label="Remover foto"
                            className="absolute top-2 right-2 w-8 h-8 flex items-center justify-center rounded-full bg-black/50 text-white text-sm hover:bg-black/70"
                        >
                            ✕
                        </button>
                    </div>
                ) : (
                    <button
                        type="button"
                        onClick={() => fileInput.current?.click()}
                        className="mt-3 w-full py-2.5 border border-dashed border-primary text-primary rounded-xl text-sm font-semibold flex items-center justify-center gap-2 hover:bg-primary/10 transition-colors"
                    >
                        <svg viewBox="0 0 24 24" className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2">
                            <path d="M3 7h4l2-2h6l2 2h4v13H3z" />
                            <circle cx="12" cy="13" r="3.5" />
                        </svg>
                        Adicionar foto
                    </button>
                )}
                {photoError && <p className="text-xs text-red-600 mt-1">{photoError}</p>}

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