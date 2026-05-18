# Instalación y despliegue — CentroPlus Connect

## Requisitos

```text
JDK 17+
Maven 3.8+
SQLite3
Docker (opcional, para despliegue completo)
```

## Comprobar entorno

```bash
java -version   # debe mostrar 17 o superior
mvn -version
sqlite3 --version
```

---

## 1. Clonar el repositorio

```bash
git clone https://github.com/TUUSUARIO/centroplus-connect.git
cd centroplus-connect
```

---

## 2. Crear la base de datos

```bash
cd database
sqlite3 centroplus.db < schema.sql
sqlite3 centroplus.db < seed.sql
cp centroplus.db centroplus-backup.db
cd ..
```

---

## 3. Ejecutar la app móvil JavaFX

```bash
cd mobile-app
mvn clean javafx:run
```

---

## 4. Ejecutar tests

```bash
cd mobile-app
mvn clean test
```

El informe JaCoCo se genera en:
```text
target/site/jacoco/index.html
```

---

## 5. Despliegue con Docker

```bash
docker-compose up -d
```

Servicios disponibles:
- API REST: http://localhost:8080
- Web: http://localhost:80
- Base de datos MariaDB: puerto 3306
