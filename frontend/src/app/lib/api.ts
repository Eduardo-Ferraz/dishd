import { cookies } from "next/headers";

/**
 * Cliente HTTP server-side para a API do Dishd.
 *
 * Roda apenas no servidor (server components / server actions). Le o token JWT
 * do cookie httpOnly e o envia como `Authorization: Bearer` quando presente.
 * Como as chamadas sao servidor->backend, nao passam pelo CORS do browser.
 */

const BASE_URL = process.env.API_URL ?? "http://localhost:8080";

export const TOKEN_COOKIE = "token";

interface ApiErrorBody {
  message?: string;
  fieldErrors?: Record<string, string>;
}

/** Erro lancado quando a API responde com status >= 400. */
export class ApiError extends Error {
  readonly status: number;
  readonly fieldErrors?: Record<string, string>;

  constructor(status: number, message: string, fieldErrors?: Record<string, string>) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.fieldErrors = fieldErrors;
  }
}

interface ApiOptions extends Omit<RequestInit, "body"> {
  /** Corpo JSON (serializado automaticamente). */
  body?: unknown;
}

async function bearerHeader(): Promise<Record<string, string>> {
  const token = (await cookies()).get(TOKEN_COOKIE)?.value;
  return token ? { Authorization: `Bearer ${token}` } : {};
}

/** Faz uma requisicao a API e retorna o corpo JSON tipado (ou undefined em 204). */
export async function api<T>(path: string, options: ApiOptions = {}): Promise<T> {
  const { body, headers, ...rest } = options;

  const finalHeaders: Record<string, string> = { ...(headers as Record<string, string>) };
  if (body !== undefined) {
    finalHeaders["Content-Type"] = "application/json";
  }
  Object.assign(finalHeaders, await bearerHeader());

  const res = await fetch(`${BASE_URL}${path}`, {
    ...rest,
    headers: finalHeaders,
    body: body !== undefined ? JSON.stringify(body) : undefined,
    cache: "no-store",
  });

  if (res.status === 204) {
    return undefined as T;
  }

  const text = await res.text();
  const data = text ? JSON.parse(text) : null;

  if (!res.ok) {
    const err = (data ?? {}) as ApiErrorBody;
    throw new ApiError(res.status, err.message ?? `Erro ${res.status}`, err.fieldErrors);
  }

  return data as T;
}
