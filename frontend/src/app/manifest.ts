import type { MetadataRoute } from "next";

// Rota nativa do Next (App Router): gera /manifest.webmanifest automaticamente.
// Sem libs externas (next-pwa etc.).
export default function manifest(): MetadataRoute.Manifest {
  return {
    name: "Dishd — Diário Gastronômico",
    short_name: "Dishd",
    description:
      "Plataforma social de experiências gastronômicas na Grande Vitória.",
    start_url: "/",
    scope: "/",
    display: "standalone",
    orientation: "portrait",
    background_color: "#F5EEE7",
    theme_color: "#D2691E",
    lang: "pt-BR",
    icons: [
      {
        src: "/icon-192.png",
        sizes: "192x192",
        type: "image/png",
        purpose: "any",
      },
      {
        src: "/icon-512.png",
        sizes: "512x512",
        type: "image/png",
        purpose: "any",
      },
      {
        src: "/icon-maskable-512.png",
        sizes: "512x512",
        type: "image/png",
        purpose: "maskable",
      },
    ],
  };
}
