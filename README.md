<div align="justify">

# CentroPlus Connect

Plataforma de gestión de un centro académico y deportivo.  
Proyecto Intermodular · 1º DAM.

---

## Integrantes del grupo

| Nombre | GitHub | Rol |
|--------|--------|-----|
| Iván Mesa Domínguez | @IvnMD | Backend / API REST |
| Iván Mesa Domínguez | @IvnMD | App JavaFX |
| Iván Mesa Domínguez | @IvnMD | Base de datos / Docker |
| Iván Mesa Domínguez | @IvnMD | Web HTML / Documentación |

---

## Tecnologías

| Componente | Tecnología |
|---|---|
| App móvil | JavaFX 21 |
| Backend | Java 17 |
| Base de datos | SQLite / MariaDB |
| Web | HTML + CSS + JavaScript |
| Despliegue | Docker |
| Repositorio | GitHub |

---

## Estructura del proyecto

```text
centroplus-connect/
├── backend-api/
├── mobile-app/
├── web-html/
├── database/
│   ├── schema.sql
│   ├── seed.sql
│   └── centroplus.db
├── docs/
│   ├── API.md
│   ├── INSTALLATION.md
│   └── PROJECT_REPORT.md
├── docker-compose.yml
└── README.md
```

---

## Instalación rápida

### Crear la base de datos

```bash
cd database
sqlite3 centroplus.db < schema.sql
sqlite3 centroplus.db < seed.sql
```

### Ejecutar la app móvil

```bash
cd mobile-app
mvn clean javafx:run
```

### Ejecutar tests

```bash
mvn clean test
```

---

## Ramas del proyecto

```text
main            ← versiones estables
develop         ← integración
feature/backend
feature-mobile
feature-web
feature-database
feature-docker
feature-docs
```

---

## Arquitectura

```text
App JavaFX
    ↓
API REST
    ↓
Servicios
    ↓
Repositorios
    ↓
Base de datos SQLite/MariaDB

Web HTML
    ↓
API REST
```

</div>
