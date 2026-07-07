import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import { TOKEN_COOKIE } from "./app/lib/api";

/**
 * Protege rotas autenticadas: sem cookie de token -> redireciona para /login.
 * (Server Actions tambem revalidam auth no backend via Bearer; isto e apenas o guard de navegacao.)
 */
export function proxy(request: NextRequest) {
  const token = request.cookies.get(TOKEN_COOKIE)?.value;
  if (!token) {
    const loginUrl = new URL("/login", request.url);
    return NextResponse.redirect(loginUrl);
  }
  return NextResponse.next();
}

export const config = {
  matcher: ["/home/:path*", "/minhas-avaliacoes/:path*"],
};
