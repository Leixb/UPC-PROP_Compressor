---
geometry: margin=2cm
lang: es
---

\tableofcontents

# Explicación de los juegos de pruebas

## Prueba 1

**Descripción:** Archivo de texto vacío.

**Objetivos:** Comprobar que al leer un documento con los algoritmes LZ se 
controla la posibilidad de intentar acceder a información inexistente y que
efectivamente se activa la excepción adecuada.

**Entrada:** Blank.txt

**Salida:** Tiempo de compresión/descompresión, desinflación/inflación respecto
al archivo de entrada, velocidad de compresión/descompresión y opción de 
visualizar el resultado.

**Efectos secundarios:** Creación del archivo comprimido (de mayor tamaño que 
el original)/descomprimido correspondiente.

## Prueba 2

**Descripción:** Archivo de texto grande (17,9MB).

**Objetivos:** Comprobar la capacidad de compresión de los LZs y que el 
diccionario utilizado en el caso de LZ78 y LZW es suficientemente grande y que
en caso de overflow, éste esta controlado.

**Entrada:** Large.txt

**Salida:** Tiempo de compresión/descompresión, desinflación/inflación respecto
al archivo de entrada, velocidad de compresión/descompresión y opción de 
visualizar el resultado.

**Efectos secundarios:** Creación del archivo comprimido/descomprimido 
correspondiente.

## Prueba 3

**Descripción:** Imagen ppm de tamaño estandar (3MB).

**Objetivos:** Probar los algoritmo de compresión y descompresión con una 
imagen de tamaño estandar.

**Entrada:** cliff.ppm y en caso del JPEG, la calidad de compresión deseada.

**Salida:** Tiempo de compresión/descompresión, desinflación/inflación respecto
al archivo de entrada, velocidad de compresión/descompresión y opción de 
visualizar el resultado.

**Efectos secundarios:** Creación del archivo comprimido/descomprimido 
correspondiente.

## Prueba 4

**Descripción:** Imagen ppm de gran tamaño (47,8MB).

**Objetivos:** Probar los algoritmo de compresión y descompresión con una 
imagen de gran tamaño.

**Entrada:** lake.ppm y en caso del JPEG, la calidad de compresión deseada.

**Salida:** Tiempo de compresión/descompresión, desinflación/inflación respecto
al archivo de entrada, velocidad de compresión/descompresión y opción de 
visualizar el resultado.

**Efectos secundarios:** Creación del archivo comprimido/descomprimido 
correspondiente.

## Prueba 5

**Descripción:** Archivo pdf de gran tamaño (9,4MB).

**Objetivos:** Comprobar el correcto funcionamiento de los algoritmos LZs con 
archivos de tipo pdf.

**Entrada:** HolyBible.pdf

**Salida:** Tiempo de compresión/descompresión, desinflación/inflación respecto
al archivo de entrada, velocidad de compresión/descompresión y opción de 
visualizar el resultado.

**Efectos secundarios:** Creación del archivo comprimido/descomprimido 
correspondiente.

## Prueba 6

**Descripción:** Archivo mp3 de tamaño mediano (8,2MB 4:02 min).

**Objetivos:** Comprobar el correcto funcionamiento de los algoritmos LZs y su 
capacidad de compresión con archivos tipo mp3.

**Entrada:** marenostrum.mp3 

**Salida:** Tiempo de compresión/descompresión, desinflación/inflación respecto
al archivo de entrada, velocidad de compresión/descompresión y opción de 
visualizar el resultado.

**Efectos secundarios:** Creación del archivo comprimido/descomprimido 
correspondiente.

## Prueba 7

**Descripción:** Archivo mp4 de tamaño medio (11,5MB 3:54 min).

**Objetivos:** Comprobar el correcto funcionamiento de los algoritmos LZs y su
capacidad de compresión con archivos tipo mp4.

**Entrada:** Gatos_Vs_Pepinos.mp4

**Salida:** Tiempo de compresión/descompresión, desinflación/inflación respecto
al archivo de entrada, velocidad de compresión/descompresión y opción de 
visualizar el resultado.

**Efectos secundarios:** Creación del archivo comprimido/descomprimido 
correspondiente.

## Prueba 8

**Descripción:** Carpeta con poco contenido, un texto pequeño y una imagen ppm
 (4,4MB en total).

**Objetivos:** Comprobar el correcto funcionamiento de los algoritmos, la 
funcionalidad de escoger algoritmo automáticamente y la de comprimir carpetas.

**Entrada:** Prueba 8

**Salida:** Tiempo de compresión/descompresión, desinflación/inflación respecto
al archivo de entrada, velocidad de compresión/descompresión y opción de 
visualizar el resultado.

**Efectos secundarios:** Creación del archivo comprimido/descomprimido 
correspondiente.

## Prueba 9

**Descripción:** Carpeta con todas la pruebas anteriores, incluyendo una 
carpeta vacia (102MB).

**Objetivos:** Comprobar el correcto funcionamiento de los algoritmos, la 
funcionalidad de escoger algoritmo atomáticamente, comprimir carpetas (aunque 
estén vacías) y comprimir archivos conteniendo caracteres como la "ñ" 
en el path.

**Entrada:** Prueba 9

**Salida:** Tiempo de compresión/descompresión, desinflación/inflación respecto
al archivo de entrada, velocidad de compresión/descompresión y opción de 
visualizar el resultado.

**Efectos secundarios:** Creación del archivo comprimido/descomprimido 
correspondiente.
