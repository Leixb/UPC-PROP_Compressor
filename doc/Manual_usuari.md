# Manual de usuario

## Uso de la aplicación

Compilar aplicación: 
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

### Aplicación des del terminal (no GUI)

Para ejecutar la aplicación usando el main de la primera entrega:
```bash
make run_console
```

(Se tiene que haber compilado antes con `make`)

## JUnit5

Compilar y ejecutar tests JUnit: 
```bash 
# compilar tests:
make build_test 
# ejecutar tests:
make run_test 
```

## Drivers

Para compilar y ejecutar los drivers: 
```bash 
# Build de los drivers
make build_drivers
# Ejecutar driver (ej. JPEG):
make run_JPEG_driver
```
