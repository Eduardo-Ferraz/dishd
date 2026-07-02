import Link from "next/link";
import Logo from "./components/Logo";
import SocialLogin from "./components/SocialLogin";

export default function WelcomePage() {
    return (
        <main className="min-h-screen flex flex-col relative">
            <div className="flex-1 flex flex-col justify-end items-center px-8 pb-10 bg-white">
                <div className="w-full max-w-sm flex flex-col gap-8">
                    <Logo subtitle="Ranqueie seus restaurantes preferidos" />
                    <div className="w-full flex flex-col gap-3">
                        <Link href="/login" className="w-full py-3 bg-primary text-white text-center rounded-xl font-semibold text-sm hover:bg-primary/90 transition-colors">
                            Entrar
                        </Link>
                        <Link href="/cadastro" className="w-full py-3 bg-secundary text-gray-700 text-center rounded-xl font-semibold text-sm hover:bg-secundary/80 transition-colors">
                            Cadastrar
                        </Link>
                    </div>
                </div>
            </div>
            <div className="bg-background px-8 pt-6 pb-10 flex flex-col items-center">
                <SocialLogin />
            </div>
        </main>
    );
}