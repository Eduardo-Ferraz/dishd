// Tipos espelhando os DTOs do backend (com.dishd.dto).
// Mantidos em sincronia manual com a API REST.

export type Reacao = "LIKE" | "DISLIKE";

export interface UsuarioDTO {
  id: number;
  username: string;
  nome: string;
  email: string;
  telefone: string | null;
}

export interface CategoriaDTO {
  id: number;
  nome: string;
}

export interface RestauranteDTO {
  id: number;
  nome: string;
  endereco: string | null;
  notaMedia: number;
  qntdAvaliacoes: number;
  fotoUrl: string | null;
  categorias: CategoriaDTO[];
}

export interface AvaliacaoDTO {
  id: number;
  nota: number;
  comentario: string | null;
  fotoUrl: string | null;
  favorito: boolean;
  criadoEm: string;
  usuarioId: number;
  usuarioUsername: string;
  usuarioNome: string;
  restauranteId: number;
  restauranteNome: string;
  restauranteFotoUrl: string | null;
  likes: number;
  dislikes: number;
  minhaReacao: Reacao | null;
}

export interface EstatisticasDTO {
  totalAvaliacoes: number;
  notaMedia: number;
  totalFavoritos: number;
  totalRestaurantesVisitados: number;
  totalLikesRecebidos: number;
  categoriaFavorita: string | null;
}

export interface PagedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface AuthResponse {
  token: string;
  tipo: string;
  usuario: UsuarioDTO;
}
