# El Compressor

Projecte PROP Quatrimestre tardor 2019/20

## Miembros del grupo

- ***REMOVED*** <***REMOVED***>
- ***REMOVED*** <***REMOVED***>
- ***REMOVED*** <***REMOVED***>
- ***REMOVED*** <***REMOVED***>

## Lista de classes implementadas por cada miembro:

- **JPEG**: ***REMOVED***
- **LZ78**: ***REMOVED***
- **LZW**: ***REMOVED***
- **LZSS**: Alber Mercadé

- **JPEGBlock**: ***REMOVED***
- **Huffman**: ***REMOVED***
- **PpmImage**: ***REMOVED***
- **BitSetL**: ***REMOVED***
- **IO**: ***REMOVED***
- **Statistics**: ***REMOVED***
- **CtrlDomini**: ***REMOVED***
- **CtrlPresentacio**:  ***REMOVED***
- **Main**: ***REMOVED***

## Uso de la aplicación

Compilar aplicacion:
```bash
make
```

Ejecutar:
```bash
make run
```
Crear jar (`target/ProjecteProp-1.0-SNAPSHOT.jar`):
```bash
make jar
```

### JUnit5

Compilar tests JUnit:
```bash
make build_test
```
Ejecutar tests JUnit:
```bash
make run_test
```

### Drivers
Para compilar y ejecutar los drivers:
```bash
bash ./buildRunDriver.sh NombreDeClasseAProbar
# Ejemplo para JPEGDriver:
bash ./buildRunDriver.sh JPEG
```

## Diagramas

### Diagrama Casos d'Us

![Diagrama Casos d'us](doc/UML/CasosUs.png)

### Diagrames de Classes

![Diagrama Classes](doc/UML/DiagramaClasses.png)
![Diagrama Classes IO](doc/UML/DiagramaClasses_IO.png)
![Diagrama Classes JPEG](doc/UML/DiagramaClasses_JPEG.png)
