---
geometry: margin=2cm
lang: es
---

\tableofcontents

# Estructuras de Datos & Algoritmos

## LZSS

### Compresión

#### Estructuras de Datos

Para la ventana deslizante que contiene los últimos $n$ caracteres leídos del fichero usamos un ArrayList ya que es modificable su tamaño y también permite recorrerlo para encontrar coincidencias.

Para guardar los caracteres que tienen coincidencia hasta el momento hemos usado también un ArrayList  por las misma razones que la ventana deslizante.

#### Algoritmos

El único algoritmo que usamos en esta implementación de LZSS es Knuth-Morris-Pratt ya que aumenta un poco la eficiencia para encontrar coincidencias de un patrón dentro de la ventana deslizante. Lo hemos adaptado para que devuelva el indice empezando por el final de la primera ocurrencia del patrón, y si el patrón no tiene ocurre en la ventana simplemente retorna -1.

### Descompresión

#### Estructuras de Datos

Al igual que en la compresión y por la mismas razones usamos un ArrayList para guardar la ventana deslizante.

#### Algoritmos

Para la implementación de la descompresión no usamos ningún algoritmo auxiliar de importancia.

## LZ78

### Compresión

#### Estructuras de Datos

Para poder comprimir todo tipo de archivos de manera óptima con el algoritmo LZ78, hemos utilizado lectura de bytes y un Tree compuesto por Nodos. Cada Nodo tiene un índice int, que indica el orden de inclusión en el arbol y por lo tanto el orden de aparición en el archivo que se desea comprimir, y 256 hijos ya que al realizar un lectura de tipo byte hay 256 posibles valores.
Para controlar el Overflow así como la detección del final del archivo nos hemos decantado por añadir un bit delante de cada array de bytes codificado, de manera que la composición de la codificación de un byte consta de: 
- Un bit. 0 indica que se continua sin problemas, 1 que a continuación hay un overflow o fin del archivo.
- Si el bit anterior era 0, el indice del padre del byte codificado. Si era 1, en caso de oveflow un 0 y en caso de fin del archivo un 1.
- El byte codificado.

Cada codificación se guarda en un ArrayList hasta que éste se llena o se acaba el archivo, entonces se recorre y se escribe en el fichero comprimido. En caso de overflow se crea un nuevo Tree y un nuevo ArrayList.
 
### Descompresión

#### Estructuras de Datos

Para descomprimir hemos utilizado en este caso un HashMap con key Integer y value ArrayList <Byte> actuando como diccionario. Al descomprimir se lee primero un bit, que indica si hay overflow o fin de archivo, un numero, que indica una entrada del diccionario y a continuación se lee el byte del que va acompañado dicho numero. Finalmente se escribe en el archivo de descompresión la concatenación del value correspondiente a la entrada del diccionario junto con el byte leído.

## LZW

### Compresión

#### Estructuras de Datos

Para comprimir en el algoritmo LZW hemos utilizado un HashMap con key ArrayList<Byte> y value Integer, actuando de diccionario y de manera que todos los bytes que se leen del archivo input quedan guardados en este. El diccionario se ha de inicializar con los valores unitarios esperados en el archivo a comprimir y a medida que se vaya leyendo la entrada se irán añadiendo podibles combinaciones de éstos. En caso preveer overflow del diccionario se escribe en el archivo comprimido un int indicativo, de manera que al descomprimir se pueda saber en que momento se ha reiniciado el diccionario. Al acabar la descompresión se vacía HashMap para evitar problemas de exceso de memoria con siguientes compresiones.

### Descompresión

#### Estructuras de Datos

Para descomprimir hemos utilizado en este caso un HashMap inverso al de compresión, con key Integer y value ArrayList<Byte>, actuando también como diccionario. Al igual que en la compresión el diccionario se ha de inicializar con los valores unitarios esperados en el archivo a descomprimir y a medida que se vaya leyendo la entrada se irán añadiendo podibles combinaciones de éstos. En caso de lectura del integer que indica overflow se reinicia el diccionario. Al acabar la descompresión se vacía el HashMap para evitar problemas de exceso de memoria en siguientes compresiones.

## JPEG

### Compresión

#### Estructuras de Datos

Para la compresión se ha usado una tabla Huffman con valores predefinidos.

#### Algoritmos

- *DCT (Discrete Cosine Transform)* Transforma los datos de la imagen como sumas de cosenos bidimensionales.
- *RLE (Run Length Encoding)* Codifica los datos según el número de 0 que lo preceden (Run) y los bits necesarios para representar-lo (Length). Los valores de Run y length son los que se codifican con la tabla Huffman. Gracias a la DCT, hay muchos 0 lo que permite comprimir los datos con RLE.

### Descompresión

#### Estructuras de Datos

Para descomprimir el archivo, se ha usado la misma tabla de Huffman que en la compresión pero en formato de árbol.

#### Algoritmos

- *inverse DCT (Discrete Cosine Transform)* Deshace la DCT de la compresión
- *RLE (Run Length Encoding)* Deshace el RLE de la compresión.
