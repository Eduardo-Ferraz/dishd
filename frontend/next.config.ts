import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  // Raiz do workspace = esta pasta (evita warning por múltiplos lockfiles no monorepo).
  turbopack: {
    root: __dirname,
  },
  experimental: {
    // Fotos vão em base64 no corpo da Server Action — folga acima do limite padrão (1MB).
    serverActions: { bodySizeLimit: "3mb" },
  },
  images: {
    remotePatterns: [
      {
        protocol: "https",
        hostname: "images.unsplash.com",
        port: "",
        pathname: "/**",
      },
      {
        // Imagens de exemplo geradas pelo DataSeeder do backend.
        protocol: "https",
        hostname: "picsum.photos",
        port: "",
        pathname: "/**",
      },
    ],
  },
};

export default nextConfig;
