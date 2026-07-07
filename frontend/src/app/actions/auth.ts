"use server";

import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import { api, ApiError, TOKEN_COOKIE } from "../lib/api";
import type { AuthResponse } from "../lib/types";

export interface AuthState {
  error?: string;
}

// 1 dia, alinhado ao dishd.jwt.expiration-ms do backend (86400000ms).
const COOKIE_OPTIONS = {
  httpOnly: true,
  sameSite: "lax" as const,
  path: "/",
  maxAge: 60 * 60 * 24,
  secure: process.env.NODE_ENV === "production",
};

async function persistToken(token: string) {
  (await cookies()).set(TOKEN_COOKIE, token, COOKIE_OPTIONS);
}

/** Server Action de login. Usada com useActionState. */
export async function login(_prev: AuthState, formData: FormData): Promise<AuthState> {
  const email = String(formData.get("email") ?? "").trim();
  const password = String(formData.get("password") ?? "");

  let res: AuthResponse;
  try {
    res = await api<AuthResponse>("/api/auth/login", {
      method: "POST",
      body: { email, password },
    });
  } catch (e) {
    return { error: e instanceof ApiError ? e.message : "Falha ao entrar. Tente novamente." };
  }

  await persistToken(res.token);
  redirect("/home");
}

/** Server Action de cadastro. Usada com useActionState. */
export async function register(_prev: AuthState, formData: FormData): Promise<AuthState> {
  const username = String(formData.get("username") ?? "").trim();
  const nome = String(formData.get("nome") ?? "").trim();
  const email = String(formData.get("email") ?? "").trim();
  const telefone = String(formData.get("telefone") ?? "").trim();
  const password = String(formData.get("password") ?? "");
  const confirma = String(formData.get("confirma") ?? "");

  if (password !== confirma) {
    return { error: "As senhas nao conferem." };
  }

  let res: AuthResponse;
  try {
    res = await api<AuthResponse>("/api/auth/register", {
      method: "POST",
      body: { username, nome, email, telefone: telefone || null, password },
    });
  } catch (e) {
    return { error: e instanceof ApiError ? e.message : "Falha no cadastro. Tente novamente." };
  }

  await persistToken(res.token);
  redirect("/home");
}

/** Server Action de logout: apaga o cookie e volta para a tela inicial. */
export async function logout(): Promise<void> {
  (await cookies()).delete(TOKEN_COOKIE);
  redirect("/");
}
