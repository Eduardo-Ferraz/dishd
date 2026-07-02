"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import Logo from "../components/Logo";
import SocialLogin from "../components/SocialLogin";

export default function CadastroPage() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [confirma, setConfirma] = useState("");
  const [celular, setCelular] = useState("");
  const [termos, setTermos] = useState(false);

  const canSubmit = email && senha && confirma && celular && termos && senha === confirma;

  function handleCadastro() {
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
            <input
              type="password"
              placeholder="Senha novamente"
              value={confirma}
              onChange={(e) => setConfirma(e.target.value)}
              className="w-full px-4 py-3 border border-primary rounded-xl text-sm text-gray-700 placeholder-primary-text focus:outline-none focus:ring-1 focus:ring-primary"
            />
            <input
              type="tel"
              placeholder="Celular"
              value={celular}
              onChange={(e) => setCelular(e.target.value)}
              className="w-full px-4 py-3 border border-primary rounded-xl text-sm text-gray-700 placeholder-primary-text focus:outline-none focus:ring-1 focus:ring-primary"
            />

            <label className="flex items-center gap-2 cursor-pointer">
              <input
                type="checkbox"
                checked={termos}
                onChange={(e) => setTermos(e.target.checked)}
                className="w-4 h-4 accent-primary"
              />
              <span className="text-xs text-primary-text">Li e concordo com os termos e serviços</span>
            </label>

            <button
              onClick={handleCadastro}
              disabled={!canSubmit}
              className={`w-full py-3 rounded-xl font-semibold text-sm transition-colors mt-1 ${
                canSubmit
                  ? "bg-primary text-white hover:bg-primary/90"
                  : "bg-secundary text-primary-text cursor-not-allowed"
              }`}
            >
              Cadastrar
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