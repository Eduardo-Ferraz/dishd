import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  // Raiz do workspace = esta pasta (evita warning por múltiplos lockfiles no monorepo).
  turbopack: {
    root: __dirname,
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
