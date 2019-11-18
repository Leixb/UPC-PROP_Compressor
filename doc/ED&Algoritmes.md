[toc]

#Estructuras de Datos & Algoritmos

## LZSS

###Compresión

####Estructuras de Datos

Para la ventana deslizante que contiene los últimos $n$ caracteres leidos del fichero usamos un ArrayList ya que es modificable su tamaño y también permite recorrerlo para encontrar coincidencias.

Para guardar los caracteres que tienen coincidencia hasta el momento hemos usado también un ArrayList  por las misma razones que la ventana deslizante.

#### Algoritmos

El único algoritmo que usamos en esta implementación de LZSS es Knuth-Morris-Pratt ya que augmenta un poco la eficiencia para encontrar coincidencias de un patron dentro de la ventana deslizante. Lo hemos adaptado para que devuelva el indice empezando por el final de la primera ocurrencia del patron, y si el patron no tiene ocurre en la ventana simplemente retorna -1.

### Descompresión

#### Estructuras de Datos

Al igual que en la compresión y por la mismas razones usamos un ArrayList para guardar la ventana deslizante.

#### Algoritmos

Para la implementación de la descompresión no usamos ningún algoritmo auxiliar de importancia.

## LZ78

### Compresión

#### Estrucuras de Datos

Para comprimir en el algoritmo LZ78 hemos utilizado un HashMap con key String y value Integer, actuando de diccionario y de manera que todos los caràcteres que se leen del archivo input quedan guardados en este. En caso que ya se hayan guardado se guarda un string concatenando la entrada del diccionario con el siguiente caracter leído.
Como archivo comprimido resultante se obtiene un conjunto de sets compuestos por un numero y un caracter.

### Descompresión

#### Estructuras de Datos

Para descomprimir hemos utilizado en este caso un HashMap inverso al de compresión, con key Integer y value String, actuando también como diccionario. Al descomprimir se lee primero un numero, que indica una entrada del diccionario y a continuación se lee el caracter del que va acompañado dicho numero. Finalmente se escribe en el archivo de descompresión la concatencación del value correspondiente a la entrada del diccionario junto con el char leídos.