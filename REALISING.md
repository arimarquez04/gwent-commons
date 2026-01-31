# Releasing & Versioning – gwent-commons

Este documento define **cómo versionar, crear tags y publicar releases**
del proyecto **gwent-commons**, un **repo Maven multi-módulo** usado como
dependencia por otros proyectos.

El objetivo es:
- versiones claras
- releases reproducibles
- historial entendible
- consumo seguro desde otros servicios

---

## 📦 Estructura del proyecto

```text
gwent-commons
├── gwent-domain
├── gwent-contracts
├── gwent-common
└── pom.xml (parent)
```
Todos los módulos comparten **la misma versión**, definida en el `pom.xml` padre.

---

## 🧭 Convención de versionado (SemVer)

Se usa **Semantic Versioning**:

MAJOR.MINOR.PATCH

### Reglas
- **MAJOR**: cambios incompatibles (rompen API pública)
- **MINOR**: nuevas funcionalidades compatibles
- **PATCH**: fixes compatibles

### Ejemplos
- `0.1.1` → bugfix
- `0.2.0` → nueva feature
- `1.0.0` → API estable

---

## 🏷️ Relación Maven ↔ Git

| Maven Version | Git Tag |
|--------------|---------|
| `0.1.0` | `v0.1.0` |
| `0.2.0` | `v0.2.0` |
| `1.0.0` | `v1.0.0` |

👉 **Siempre deben coincidir**

---

## 📌 ¿Qué es una tag?

Una **tag** es un marcador **inmutable** que apunta a un commit exacto.

> “Este commit representa una versión estable del proyecto”.

Las tags sirven para:
- identificar releases
- volver a versiones estables
- crear GitHub Releases
- automatizar CI/CD

---

## 📋 Checklist antes de crear una versión estable

Antes de taggear:

- [ ] Estar en branch `main`
- [ ] `git status` limpio
- [ ] Código compila
- [ ] Tests pasan (si existen)
- [ ] Versión Maven correcta
- [ ] `CHANGELOG.md` actualizado
- [ ] Commit de versión creado

---

## 🚀 Paso a paso: publicar una versión estable

### Ejemplo: publicar `0.2.0`

---

### 1️⃣ Actualizar rama local

```powershell
git checkout main
git pull
```
### 2️⃣ Verificar estado
```powershell
git status
```
### Debe indicar:
```powershell
working tree clean
```
### 3️⃣ Build del proyecto
```powershell
mvn clean test
```
### Si no hay tests todavía:
```powershell
mvn clean package
```

### 4️⃣ Actualizar versión Maven

La versión se actualiza solo en el ```
pom.xml``` padre.

#### Opción A: manual

Editar el ```
pom.xml```  raíz:
```xml
<version>0.2.0</version>
```
#### Opción B: Maven Versions Plugin
```powershell
mvn versions:set -DnewVersion=0.2.0
mvn versions:commit
```

### 5️⃣ Commit del cambio de versión
```powershell
git add -A
git commit -m "chore(release): 0.2.0"
```
### 6️⃣ Crear el tag (anotado)
```powershell
git tag -a v0.2.0 -m "Release 0.2.0"
```
Usar siempre tags anotados para releases.


### 7️⃣ Subir commit y tag
```powershell
git push
git push origin v0.2.0
```

O bien:
```powershell
git push --tags
```

### 8️⃣ Verificar el tag
```powershell
git tag
git show v0.2.0
```


---

## 🧩 Responsabilidad por módulo

--- 

```gwent-domain```

- Entidades

- Enums

- Value Objects

- Reglas de dominio compartidas

🔴 Cambios aquí suelen impactar en MAJOR/MINOR.

---

```gwent-contracts ```

- DTOs

- Requests / Responses

- Anotaciones de serialización

- Validaciones

🔴 Cambios rompen contratos → cuidado con compatibilidad.

---

```gwent-common```

-  Utilidades

- Excepciones compartidas

- Helpers comunes

- Integraciones genéricas

🟢 Generalmente cambios compatibles.

---

## 🧨 Cómo borrar un tag (si te equivocaste)

Borrar local
```powershell
git tag -d v0.2.0
```

Borrar remoto
```powershell
git push origin :refs/tags/v0.2.0
```
---
Luego podés recrearlo correctamente.

🧠 Flujo resumido recomendado
```text
git pull
mvn clean test
update version (pom.xml)
git commit -m "chore(release): X.Y.Z"
git tag -a vX.Y.Z -m "Release X.Y.Z"
git push && git push origin vX.Y.Z
```
---
## 📎 Nota final

Este repo es una librería base para otros proyectos.
Las versiones deben ser:

- predecibles

- estables

- compatibles

Una buena estrategia de tags evita problemas s futuro.