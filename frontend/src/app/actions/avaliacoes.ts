"use server";
import { revalidatePath } from "next/cache";
import { api, ApiError } from "../lib/api";
import type { Reacao } from "../lib/types";

export interface AvaliacaoInput {
  restauranteId: number;
  nota: number;
  comentario: string;
  favorito: boolean;
  fotoUrl?: string | null;
}

export interface ActionResult {
  error?: string;
}

function revalidar(revalidate?: string) {
  if (revalidate) revalidatePath(revalidate);
  revalidatePath("/home");
  revalidatePath("/minhas-avaliacoes");
}

function toError(e: unknown): ActionResult {
  if (e instanceof ApiError) return { error: e.message };
  return { error: "Erro inesperado. Tente novamente." };
}

export async function criarAvaliacao(input: AvaliacaoInput, revalidate?: string): Promise<ActionResult> {
  try {
    await api("/api/avaliacoes", {
      method: "POST",
      body: { ...input, fotoUrl: input.fotoUrl ?? null },
    });
  } catch (e) {
    return toError(e);
  }
  revalidar(revalidate);
  return {};
}

export async function atualizarAvaliacao(id: number, input: AvaliacaoInput, revalidate?: string): Promise<ActionResult> {
  try {
    await api(`/api/avaliacoes/${id}`, {
      method: "PUT",
      body: { ...input, fotoUrl: input.fotoUrl ?? null },
    });
  } catch (e) {
    return toError(e);
  }
  revalidar(revalidate);
  return {};
}

export async function excluirAvaliacao(id: number, revalidate?: string): Promise<ActionResult> {
  try {
    await api(`/api/avaliacoes/${id}`, { method: "DELETE" });
  } catch (e) {
    return toError(e);
  }
  revalidar(revalidate);
  return {};
}

/** Define (POST) ou remove (DELETE, quando tipo=null) a reacao do usuario logado. */
export async function definirReacao(id: number, tipo: Reacao | null, revalidate?: string): Promise<ActionResult> {
  try {
    if (tipo === null) {
      await api(`/api/avaliacoes/${id}/reacoes`, { method: "DELETE" });
    } else {
      await api(`/api/avaliacoes/${id}/reacoes`, { method: "POST", body: { tipo } });
    }
  } catch (e) {
    return toError(e);
  }
  revalidar(revalidate);
  return {};
}
