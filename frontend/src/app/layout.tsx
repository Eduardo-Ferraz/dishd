import type { Metadata } from "next";
import { Playfair_Display, Plus_Jakarta_Sans, Work_Sans } from "next/font/google";
import "./globals.css";

const playfair = Playfair_Display({
  subsets: ["latin"],
  variable: "--font-playfair",
});

const plusJakarta = Plus_Jakarta_Sans({
  subsets: ["latin"],
  variable: "--font-plus-jakarta",
});

const workSans = Work_Sans({
  subsets: ["latin"],
  variable: "--font-work-sans",
});

export const metadata: Metadata = {
  title: "Dishd",
  description: "Projeto Integrado - Dishd",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html 
      lang="pt-BR" 
      className={`${playfair.variable} ${plusJakarta.variable} ${workSans.variable}`}
    >
      {/* Definimos a 'font-playfair' como fonte padrão do corpo inteiro do site */}
      <body className="font-playfair antialiased bg-background">
        {children}
      </body>
    </html>
  );
}
