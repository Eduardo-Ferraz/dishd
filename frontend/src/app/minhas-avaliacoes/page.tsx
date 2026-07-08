import { redirect } from "next/navigation";
import Navbar from "../components/Navbar";
import BottomNav from "../components/BottomNav";
import Tabs from "../components/Abas";
import MinhasAvaliacoesList from "../components/MinhasAvaliacoesList";
import { api } from "../lib/api";
import { getCurrentUser } from "../lib/session";
import type { AvaliacaoDTO, PagedResponse } from "../lib/types";

export default async function MinhasAvaliacoesPage() {
  const user = await getCurrentUser();
  if (!user) {
    redirect("/login");
  }

  const resp = await api<PagedResponse<AvaliacaoDTO>>(`/api/usuarios/${user.id}/avaliacoes?size=50`);

  return (
    <main className="min-h-screen bg-background flex flex-col relative">
      <Navbar />
      <Tabs />
      <MinhasAvaliacoesList avaliacoes={resp.content} />
      <BottomNav />
    </main>
  );
}
