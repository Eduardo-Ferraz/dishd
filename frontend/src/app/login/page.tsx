"use client";
import { useActionState } from "react";
import { login, type AuthState } from "../actions/auth";
import Logo from "../components/Logo";
import SocialLogin from "../components/SocialLogin";

export default function LoginPage() {
    const [state, action, pending] = useActionState<AuthState, FormData>(login, {});

    return (
        <main className="min-h-screen flex flex-col">
            <div className="flex-1 flex flex-col justify-end items-center px-8 pb-10 bg-white">
                <div className="w-full max-w-sm flex flex-col items-center gap-8">
                <Logo />

                <form action={action} className="w-full flex flex-col gap-3">
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
                    className="w-full px-4 py-3 border border-primary rounded-xl text-sm text-gray-700 placeholder-primary-text focus:outline-none focus:ring-1 focus:ring-primary"
                    />
                    {state.error && (
                    <p className="text-xs text-red-600">{state.error}</p>
                    )}
                    <button
                    type="submit"
                    disabled={pending}
                    className="w-full py-3 bg-primary text-white rounded-xl font-semibold text-sm hover:bg-primary/90 transition-colors mt-1 disabled:opacity-60"
                    >
                    {pending ? "Entrando..." : "Entrar"}
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
