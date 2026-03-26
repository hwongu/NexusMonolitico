export interface Revenue {
  idIngreso: number;
  idUsuario: number;
  fechaIngreso: string;
  estado: string;
}

export interface RevenueDetail {
  idDetalleIngreso?: number;
  idIngreso?: number;
  idProducto: number;
  nombreProducto?: string;
  cantidad: number;
  precioCompra: number;
  stockActual?: number;
}

export interface RevenueCreateIngreso {
  idUsuario: number;
  fechaIngreso: string;
  estado: string;
}

export interface RevenueCreatePayload {
  ingreso: RevenueCreateIngreso;
  detalles: RevenueDetail[];
}
