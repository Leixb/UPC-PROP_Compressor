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

Al igual que en la compresión usamos un ArrayList para guardar la ventana deslizante.

#### Algoritmos

Para la implementación de la descompresión no usamos ningún algoritmo auxiliar.