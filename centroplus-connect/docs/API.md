# API REST — CentroPlus Connect

Base URL: `http://localhost:8080`

---

## Usuarios

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/usuarios` | Listar todos los usuarios |
| GET | `/usuarios/{id}` | Obtener usuario por ID |
| POST | `/usuarios` | Crear nuevo usuario |

---

## Actividades

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/actividades` | Listar todas las actividades |
| GET | `/actividades/{id}` | Obtener actividad por ID |

---

## Reservas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/reservas` | Listar todas las reservas |
| GET | `/reservas/{id}` | Obtener reserva por ID |
| POST | `/reservas` | Crear nueva reserva |
| DELETE | `/reservas/{id}` | Cancelar reserva |

---

## Incidencias

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/incidencias` | Listar todas las incidencias |
| POST | `/incidencias` | Registrar nueva incidencia |

---

## Ejemplo respuesta GET /actividades

```json
[
  {
    "id": 1,
    "nombre": "Yoga",
    "tipoActividad": "DEPORTIVA",
    "duracionMinutos": 60,
    "precio": 25.50,
    "plazasMaximas": 15,
    "plazasOcupadas": 8
  }
]
```
