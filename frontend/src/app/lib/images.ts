/**
 * Fallback deterministico para imagens ausentes na API (fotoUrl nulo).
 * Usa picsum.photos com seed estavel por id, liberado em next.config.
 */
export function fallbackImg(url: string | null | undefined, seed: string | number): string {
  return url ?? `https://picsum.photos/seed/dishd-${seed}/400`;
}
