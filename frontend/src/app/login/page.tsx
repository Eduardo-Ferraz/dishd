"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import Logo from "../components/Logo";
import SocialLogin from "../components/SocialLogin";

export default function LoginPage() {
    const router = useRouter();
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");

    function handleLogin() {
        // Aqui vai a chamada à API futuramente
        router.push("/home");
    }

    return (
        <main className="min-h-screen flex flex-col">
            <div className="flex-1 flex flex-col justify-end items-center px-8 pb-10 bg-white">
                <div className="w-full max-w-sm flex flex-col items-center gap-8">
                <Logo />
        
                <div className="w-full flex flex-col gap-3">
                    <input
                    type="email"
                    placeholder="E-mail"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="w-full px-4 py-3 border border-primary rounded-xl text-sm text-gray-700 placeholder-primary-text focus:outline-none focus:ring-1 focus:ring-primary"
                    />
                    <input
                    type="password"
                    placeholder="Senha"
                    value={senha}
                    onChange={(e) => setSenha(e.target.value)}
                    className="w-full px-4 py-3 border border-primary rounded-xl text-sm text-gray-700 placeholder-primary-text focus:outline-none focus:ring-1 focus:ring-primary"
                    />
                    <button
                    onClick={handleLogin}
                    className="w-full py-3 bg-primary text-white rounded-xl font-semibold text-sm hover:bg-primary/90 transition-colors mt-1"
                    >
                    Entrar
                    </button>
                </div>
                </div>
            </div>
        
            <div className="bg-background px-8 pt-6 pb-10 flex flex-col items-center">
                <SocialLogin />
            </div>
        </main>
    );
}