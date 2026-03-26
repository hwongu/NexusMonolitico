export interface User {
  idUsuario?: number;
  usuario?: string;
  username: string;
  password?: string;
  estado: boolean;
}

export interface LoginRequest {
  username: string;
  password?: string;
}
