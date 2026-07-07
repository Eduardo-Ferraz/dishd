import { cache } from "react";
import { cookies } from "next/headers";
import { api } from "./api";
import { TOKEN_COOKIE } from "./api";
import type { UsuarioDTO } from "./types";

/**
 * Usuario autenticado (via GET /api/auth/me), ou null se nao houver token valido.
 * Memoizado por requisicao com React.cache para evitar chamadas duplicadas.
 */
export const getCurrentUser = cache(async (): Promise<UsuarioDTO | null> => {
  const token = (await cookies()).get(TOKEN_COOKIE)?.value;
  if (!token) {
    return null;
  }
  try {
    return await api<UsuarioDTO>("/api/auth/me");
  } catch {
    return null;
  }
});
