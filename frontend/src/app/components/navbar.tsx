import Image from "next/image";
import Link from "next/link";

export default function Navbar(){
    return (
        <header className="flex items-center justify-between px-8 py-5 bg-cards">
            <Link className="w-12 h-12 flex items-center justify-center" href="#">
                <Image src="/dishd-icon.svg" alt="logo" width={36} height={36} />
            </Link>
            <span className="text-xl font-bold text-gray-800 tracking-wide">Dishd</span>
            <Link className="w-10 h-10" href="#">
                <Image src="/person-box.svg" alt="perfil" width={36} height={36} />
            </Link>
        </header>
    )
}