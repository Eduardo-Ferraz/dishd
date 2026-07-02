import Image from "next/image";

export default function Logo({ subtitle }: { subtitle?: string }) {
    return (
        <div className="flex flex-col gap-2">
            <div className="flex items-center gap-3">
                <Image src="/dishd-icon.svg" alt="Dishd" width={56} height={56} />
                <span className="text-3xl font-bold text-gray-800">Dishd</span>
            </div>
            {subtitle && (
                <p className="text-sm text-gray-700">{subtitle}</p>
            )}
        </div>
    );
}