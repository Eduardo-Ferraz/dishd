import Image from "next/image";
import Link from "next/link";
import { logout } from "../actions/auth";

export default function Navbar(){
    return (
        <header className="flex items-center justify-between px-8 py-4 bg-cards">
            <Link className="w-12 h-12 flex items-center justify-center" href="/home">
                <Image src="/dishd-icon.svg" alt="logo" width={36} height={36} />
            </Link>
            <span className="text-xl font-bold text-gray-800 tracking-wide">Dishd</span>
            <div className="flex items-center gap-2">
                <Link className="w-10 h-10 flex items-center justify-center" href="/minhas-avaliacoes">
                    <Image src="/person-box.svg" alt="perfil" width={36} height={36} />
                </Link>
                <form action={logout}>
                    <button
                        type="submit"
                        title="Sair"
                        className="text-xs text-primary-text hover:text-primary transition-colors"
                    >
                        Sair
                    </button>
                </form>
            </div>
        </header>
    )
}
