from __future__ import annotations

from pathlib import Path
import re
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parent.parent
REPORTS = ROOT / "target" / "surefire-reports"
TARGET = ROOT / "target"

REPOSITORY_DIR = ROOT / "src" / "main" / "java" / "es" / "ies" / "puerto" / "repositories"

# ============================================================
# PESOS DEL EXAMEN
# ============================================================
#
# FICHEROS = 4.00 puntos
#   - Actividad CSV funcionalidad: 3.50
#   - Documentación ActividadRepository: 0.50
#
# BBDD = 6.00 puntos
#   - Usuario SQLite funcionalidad: 1.50
#   - Inscripción SQLite funcionalidad: 3.50
#   - Documentación UsuarioRepository: 0.50
#   - Documentación InscripcionRepository: 0.50
#
# TOTAL = 10.00 puntos
#
# La parte más difícil tiene más peso:
#   InscripcionService > ActividadService > UsuarioService
#
# La documentación de una interfaz solo suma si el bloque correspondiente
# alcanza al menos el 85% de tests pasados.

PESOS_TESTS = {
    "actividad_csv": 3.50,
    "usuario_bbdd": 1.50,
    "inscripcion_bbdd": 3.50,
}

PESOS_DOCUMENTACION = {
    "actividad_csv": 0.50,
    "usuario_bbdd": 0.50,
    "inscripcion_bbdd": 0.50,
}

INTERFACES = {
    "actividad_csv": REPOSITORY_DIR / "ActividadRepository.java",
    "usuario_bbdd": REPOSITORY_DIR / "UsuarioRepository.java",
    "inscripcion_bbdd": REPOSITORY_DIR / "InscripcionRepository.java",
}

METODOS_ESPERADOS = {
    "actividad_csv": {
        "findAll": ["lista", "actividades"],
        "findById": ["busca", "identificador"],
        "save": ["guarda", "actividad"],
        "update": ["actualiza", "actividad"],
        "delete": ["elimina", "actividad"],
    },
    "usuario_bbdd": {
        "findAll": ["lista", "usuarios"],
        "findById": ["busca", "identificador"],
        "save": ["guarda", "usuario"],
        "update": ["actualiza", "usuario"],
        "delete": ["elimina", "usuario"],
    },
    "inscripcion_bbdd": {
        "findAll": ["lista", "inscripciones"],
        "findById": ["busca", "identificador"],
        "findByUsuario": ["usuario"],
        "findByActividad": ["actividad"],
        "save": ["guarda", "inscripcion"],
        "update": ["actualiza", "inscripcion"],
        "delete": ["elimina", "inscripcion"],
    },
}

BLOQUES_FICHEROS = ("actividad_csv",)
BLOQUES_BBDD = ("usuario_bbdd", "inscripcion_bbdd")


def detectar_bloque(nombre_clase: str) -> str:
    nombre = nombre_clase.lower()

    if "actividadservicetest" in nombre:
        return "actividad_csv"

    if "usuarioservicetest" in nombre:
        return "usuario_bbdd"

    if "inscripcionservicetest" in nombre:
        return "inscripcion_bbdd"

    return "otros"


def normalizar_texto(texto: str) -> str:
    reemplazos = str.maketrans(
        "áéíóúÁÉÍÓÚñÑ",
        "aeiouAEIOUnN"
    )
    texto = texto.translate(reemplazos).lower()
    texto = re.sub(r"\s+", " ", texto)
    return texto.strip()


def eliminar_comentarios_java_no_javadoc(texto: str) -> str:
    texto = re.sub(r"//.*", "", texto)
    return texto


def extraer_javadocs_por_metodo(interface_path: Path) -> dict[str, str]:
    if not interface_path.exists():
        return {}

    texto = interface_path.read_text(encoding="utf-8")
    texto = eliminar_comentarios_java_no_javadoc(texto)

    patron = re.compile(
        r"/\*\*(?P<javadoc>[\s\S]*?)\*/\s*"
        r"(?:public\s+)?"
        r"[\w<>\[\], ?]+\s+"
        r"(?P<metodo>\w+)\s*\(",
        flags=re.MULTILINE,
    )

    return {
        match.group("metodo"): normalizar_texto(match.group("javadoc"))
        for match in patron.finditer(texto)
    }


def puntuar_documentacion_interface(interface_path: Path, bloque: str) -> dict:
    esperados = METODOS_ESPERADOS[bloque]
    javadocs = extraer_javadocs_por_metodo(interface_path)

    encontrados = []
    faltantes = []
    sin_javadoc = []

    for metodo, palabras_clave in esperados.items():
        texto = javadocs.get(metodo)

        if texto is None:
            faltantes.append(metodo)
            sin_javadoc.append(metodo)
            continue

        if all(normalizar_texto(palabra) in texto for palabra in palabras_clave):
            encontrados.append(metodo)
        else:
            faltantes.append(metodo)

    ratio = 0.0 if not esperados else len(encontrados) / len(esperados)

    return {
        "puntos": round(ratio * PESOS_DOCUMENTACION[bloque], 2),
        "ratio": ratio,
        "encontrados": encontrados,
        "faltantes": faltantes,
        "sin_javadoc": sin_javadoc,
        "ruta": str(interface_path),
    }


def puntuar_documentacion_interfaces() -> dict:
    return {
        bloque: puntuar_documentacion_interface(ruta, bloque)
        for bloque, ruta in INTERFACES.items()
    }


def calcular_resumen_tests(report_files: list[Path]) -> dict:
    resumen = {
        bloque: {"total": 0, "passed": 0, "failed": 0}
        for bloque in PESOS_TESTS
    }

    for file in report_files:
        root = ET.parse(file).getroot()
        bloque = detectar_bloque(root.attrib.get("name", "desconocido"))

        if bloque not in resumen:
            continue

        tests = int(root.attrib.get("tests", 0))
        failures = int(root.attrib.get("failures", 0))
        errors = int(root.attrib.get("errors", 0))
        skipped = int(root.attrib.get("skipped", 0))

        failed = failures + errors + skipped
        passed = max(0, tests - failed)

        resumen[bloque]["total"] += tests
        resumen[bloque]["passed"] += passed
        resumen[bloque]["failed"] += failed

    return resumen


def calcular_nota_bloque(total: int, passed: int, peso_tests: float) -> tuple[float, float, float]:
    if total == 0:
        return 0.0, 0.0, 0.0

    ratio = passed / total
    nota_sobre_10 = round(10 * ratio, 2)
    aportacion = round(ratio * peso_tests, 2)

    return ratio, nota_sobre_10, aportacion


def puede_sumar_documentacion(total: int, passed: int) -> bool:
    return total > 0 and (passed / total) >= 0.85


def formatear_bloque(nombre: str) -> str:
    return {
        "actividad_csv": "ACTIVIDAD CSV",
        "usuario_bbdd": "USUARIO BBDD",
        "inscripcion_bbdd": "INSCRIPCION BBDD",
    }.get(nombre, nombre.upper())


def calcular_sobre_10(puntos: float, maximo: float) -> float:
    if maximo == 0:
        return 0.0
    return round((puntos / maximo) * 10, 2)


def generar_lineas_bloque(bloque: str, resumen: dict, documentacion: dict) -> tuple[list[str], float]:
    total = resumen[bloque]["total"]
    passed = resumen[bloque]["passed"]
    failed = resumen[bloque]["failed"]

    ratio, nota_sobre_10, nota_tests = calcular_nota_bloque(
        total,
        passed,
        PESOS_TESTS[bloque],
    )

    if puede_sumar_documentacion(total, passed):
        nota_doc = documentacion[bloque]["puntos"]
        doc_msg = f"{nota_doc:.2f}/{PESOS_DOCUMENTACION[bloque]:.2f}"
    else:
        nota_doc = 0.0
        doc_msg = (
            f"0.00/{PESOS_DOCUMENTACION[bloque]:.2f} "
            "(no computa por no alcanzar 85% de tests)"
        )

    subtotal = round(nota_tests + nota_doc, 2)
    max_bloque = round(PESOS_TESTS[bloque] + PESOS_DOCUMENTACION[bloque], 2)
    nombre = formatear_bloque(bloque)

    lineas = [
        f"{nombre}",
        "-" * len(nombre),
        f"Tests totales: {total}",
        f"Tests pasados: {passed}",
        f"Tests fallados: {failed}",
        f"Porcentaje tests: {ratio * 100:.2f}%",
        f"Nota tests: {nota_sobre_10:.2f}/10",
        f"Aportación tests: {nota_tests:.2f}/{PESOS_TESTS[bloque]:.2f}",
        f"Documentación interfaz: {doc_msg}",
        f"Interfaz revisada: {documentacion[bloque]['ruta']}",
    ]

    if documentacion[bloque]["faltantes"]:
        lineas.append(
            "Métodos no documentados correctamente: "
            + ", ".join(documentacion[bloque]["faltantes"])
        )
    else:
        lineas.append("Documentación de interfaz completa")

    lineas.append(f"Subtotal bloque: {subtotal:.2f}/{max_bloque:.2f}")
    lineas.append(f"Nota bloque sobre 10: {calcular_sobre_10(subtotal, max_bloque):.2f}/10")
    lineas.append("")

    return lineas, subtotal


def generar_informe() -> str:
    report_files = sorted(REPORTS.glob("TEST-*.xml"))

    resumen = calcular_resumen_tests(report_files)
    documentacion = puntuar_documentacion_interfaces()

    subtotales: dict[str, float] = {}

    lineas = [
        "=== CALIFICACION AUTOMATICA CENTROPLUS ===",
        "",
        "Pesos:",
        "  FICHEROS:",
        "    Actividad CSV: 4.00 puntos = 3.50 tests + 0.50 documentacion",
        "",
        "  BBDD:",
        "    Usuario BBDD: 1.50 tests + 0.50 documentacion = 2.00 puntos",
        "    Inscripcion BBDD: 3.50 tests + 0.50 documentacion = 4.00 puntos",
        "    Total BBDD: 6.00 puntos",
        "",
        "  TOTAL EXAMEN: 10.00 puntos",
        "",
        "Regla documentacion:",
        "  La documentacion de cada interfaz solo computa si su bloque alcanza al menos el 85% de tests pasados.",
        "",
        "Bloques:",
        "  ActividadServiceTest -> Actividad CSV",
        "  UsuarioServiceTest -> Usuario BBDD",
        "  InscripcionServiceTest -> Inscripcion BBDD",
        "",
    ]

    for bloque in ("actividad_csv", "usuario_bbdd", "inscripcion_bbdd"):
        bloque_lineas, subtotal = generar_lineas_bloque(bloque, resumen, documentacion)
        lineas.extend(bloque_lineas)
        subtotales[bloque] = subtotal

    total_ficheros = round(sum(subtotales[bloque] for bloque in BLOQUES_FICHEROS), 2)
    total_bbdd = round(sum(subtotales[bloque] for bloque in BLOQUES_BBDD), 2)
    total_final = round(total_ficheros + total_bbdd, 2)

    max_ficheros = round(
        sum(PESOS_TESTS[bloque] + PESOS_DOCUMENTACION[bloque] for bloque in BLOQUES_FICHEROS),
        2,
    )
    max_bbdd = round(
        sum(PESOS_TESTS[bloque] + PESOS_DOCUMENTACION[bloque] for bloque in BLOQUES_BBDD),
        2,
    )
    max_total = round(max_ficheros + max_bbdd, 2)

    lineas.extend([
        "=== RESUMEN FICHEROS ===",
        f"Actividad CSV: {subtotales['actividad_csv']:.2f}/4.00",
        f"Actividad CSV sobre 10: {calcular_sobre_10(subtotales['actividad_csv'], 4.00):.2f}/10",
        f"Total ficheros: {total_ficheros:.2f}/{max_ficheros:.2f}",
        f"Total ficheros sobre 10: {calcular_sobre_10(total_ficheros, max_ficheros):.2f}/10",
        "",
        "=== RESUMEN BBDD ===",
        f"Usuario BBDD: {subtotales['usuario_bbdd']:.2f}/2.00",
        f"Usuario BBDD sobre 10: {calcular_sobre_10(subtotales['usuario_bbdd'], 2.00):.2f}/10",
        f"Inscripcion BBDD: {subtotales['inscripcion_bbdd']:.2f}/4.00",
        f"Inscripcion BBDD sobre 10: {calcular_sobre_10(subtotales['inscripcion_bbdd'], 4.00):.2f}/10",
        f"Total BBDD: {total_bbdd:.2f}/{max_bbdd:.2f}",
        f"Total BBDD sobre 10: {calcular_sobre_10(total_bbdd, max_bbdd):.2f}/10",
        "",
        "=== SUMA FINAL ===",
        f"Ficheros + BBDD = {total_ficheros:.2f} + {total_bbdd:.2f} = {total_final:.2f}",
        "",
        "=== NOTA FINAL ===",
        f"Nota final: {total_final:.2f}/{max_total:.2f}",
        f"Nota final sobre 10: {calcular_sobre_10(total_final, max_total):.2f}/10",
        "",
    ])

    return "\n".join(lineas)


def main() -> int:
    informe = generar_informe()

    TARGET.mkdir(parents=True, exist_ok=True)
    salida = TARGET / "nota.txt"
    salida.write_text(informe, encoding="utf-8")

    print(informe)
    print(f"Fichero generado: {salida}")

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
