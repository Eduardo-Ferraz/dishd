"use client";
import { useActionState } from "react";
import { register, type AuthState } from "../actions/auth";
import Logo from "../components/Logo";
import SocialLogin from "../components/SocialLogin";

export default function CadastroPage() {
  const [state, action, pending] = useActionState<AuthState, FormData>(register, {});

  return (
    <main className="min-h-screen flex flex-col">
      <div className="flex-1 flex flex-col justify-end items-center px-8 pb-10 bg-white">
        <div className="w-full max-w-sm flex flex-col items-center gap-8">
          <Logo />

          <form action={action} className="w-full flex flex-col gap-3">
            <input
              type="text"
              name="nome"
              placeholder="Nome completo"
              required
              className="w-full px-4 py-3 border border-primary rounded-xl text-sm text-gray-700 placeholder-primary-text focus:outline-none focus:ring-1 focus:ring-primary"
            />
            <input
              type="text"
              name="username"
              placeholder="Nome de usuário (@)"
              required
              minLength={3}
              maxLength={50}
              className="w-full px-4 py-3 border border-primary rounded-xl text-sm text-gray-700 placeholder-primary-text focus:outline-none focus:ring-1 focus:ring-primary"
            />
            <input
              type="email"
              name="email"
              placeholder="E-mail"
              required
              className="w-full px-4 py-3 border border-primary rounded-xl text-sm text-gray-700 placeholder-primary-text focus:outline-none focus:ring-1 focus:ring-primary"
            />
            <input
              type="password"
              name="password"
              placeholder="Senha"
              required
              minLength={6}
              className="w-full px-4 py-3 border border-primary rounded-xl text-sm text-gray-700 placeholder-primary-text focus:outline-none focus:ring-1 focus:ring-primary"
            />
            <input
              type="password"
              name="confirma"
              placeholder="Senha novamente"
              required
              minLength={6}
              className="w-full px-4 py-3 border border-primary rounded-xl text-sm text-gray-700 placeholder-primary-text focus:outline-none focus:ring-1 focus:ring-primary"
            />
            <input
              type="tel"
              name="telefone"
              placeholder="Celular (opcional)"
              className="w-full px-4 py-3 border border-primary rounded-xl text-sm text-gray-700 placeholder-primary-text focus:outline-none focus:ring-1 focus:ring-primary"
            />

            <label className="flex items-center gap-2 cursor-pointer">
              <input type="checkbox" name="termos" required className="w-4 h-4 accent-primary" />
              <span className="text-xs text-primary-text">Li e concordo com os termos e serviços</span>
            </label>

            {state.error && <p className="text-xs text-red-600">{state.error}</p>}

            <button
              type="submit"
              disabled={pending}
              className="w-full py-3 rounded-xl font-semibold text-sm transition-colors mt-1 bg-primary text-white hover:bg-primary/90 disabled:opacity-60"
            >
              {pending ? "Cadastrando..." : "Cadastrar"}
            </button>
          </form>
        </div>
      </div>

      <div className="bg-background px-8 pt-6 pb-10 flex flex-col items-center">
        <SocialLogin />
      </div>
    </main>
  );
}
