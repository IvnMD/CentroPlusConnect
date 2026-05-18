# Configuración de GitHub — CentroPlus Connect

## 1. Crear el repositorio

1. Accede a https://github.com → **New repository**
2. Nombre: `centroplus-connect`
3. Tipo: Public (o Private según el profesor)
4. Activar: **Add a README file**
5. Añadir `.gitignore` → plantilla **Java**

---

## 2. Clonar y crear ramas

```bash
git clone https://github.com/TUUSUARIO/centroplus-connect.git
cd centroplus-connect

# Rama de integración
git checkout -b develop
git push -u origin develop

# Ramas de funcionalidades
git checkout develop
git checkout -b feature/backend  && git push -u origin feature/backend
git checkout develop
git checkout -b feature-mobile   && git push -u origin feature-mobile
git checkout develop
git checkout -b feature-web      && git push -u origin feature-web
git checkout develop
git checkout -b feature-database && git push -u origin feature-database
git checkout develop
git checkout -b feature-docker   && git push -u origin feature-docker
git checkout develop
git checkout -b feature-docs     && git push -u origin feature-docs
```

---

## 3. Añadir colaboradores

`Settings → Collaborators → Add people`  
Añadir a todos los miembros del grupo.

---

## 4. Crear tablero Kanban

`Projects → New Project → Board`

Columnas:
- **Pendiente**
- **En progreso**
- **En revisión**
- **Finalizado**

---

## 5. Crear Issues mínimos

| Issue | Rama | Responsable |
|-------|------|-------------|
| Crear schema.sql y seed.sql | feature-database | Miembro 3 |
| Crear modelos Java | feature/backend | Miembro 1 |
| Crear repositorios | feature/backend | Miembro 1 |
| Crear servicios | feature/backend | Miembro 2 |
| Crear pantallas JavaFX | feature-mobile | Miembro 2 |
| Crear web HTML | feature-web | Miembro 4 |
| Crear docker-compose.yml | feature-docker | Miembro 3 |
| Documentación README | feature-docs | Miembro 4 |

---

## 6. Flujo de trabajo por tarea

```
1. Crear Issue → asignar → mover a "En progreso"
2. git checkout -b feature/mi-tarea
3. Programar → commits frecuentes
4. git push
5. Pull Request → feature/... → develop
6. Revisión del equipo → Merge
7. Al finalizar todo: develop → main
```

---

## 7. Formato de commits

```bash
feat: añade ActividadService
fix: corrige cálculo de plazas disponibles
test: añade tests de ReservaService
docs: actualiza README con instrucciones de instalación
refactor: extrae validaciones a método privado
```

---

## 8. Lo que se evalúa

- Ramas creadas correctamente
- Issues con asignación y seguimiento
- Tablero Kanban actualizado
- Commits con formato profesional
- Pull Requests con revisión
- Merge final develop → main
