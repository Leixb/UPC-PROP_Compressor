# El Compressor 
[![Build Status](https://travis-ci.com/Leixb/Compressor_PROP.svg?token=qEz3Uk5fvSr9HeEEwxjb&branch=master)](https://travis-ci.com/Leixb/Compressor_PROP)
[![CodeFactor](https://www.codefactor.io/repository/github/leixb/compressor_prop/badge?s=f6f48f3e67e69da4aefc3048aba7e871deb081ed)](https://www.codefactor.io/repository/github/leixb/compressor_prop)

Projecte PROP Quatrimestre tardor 2019/20

# Xuleta git

## Clonar Repo:
```bash
git clone git@github.com:Leixb/Compressor_PROP.git
```
## Basics

### status

Mostra l estat del teu git local
(branca actual, canvis preparats per commit (staged), canvis no inclosos per al commit (unsatged) ...)
```bash
git status
```

### checkout
Canvia de branca
```bash
# si la branca ja exiteix
git checkout <nom-branca>

# si vols crear una branca no existent encara
git checout -b <nom-branca>
```

### add
Afegeix fitxer a la llista de fitxers per fer commit
``` bash
git add <nom-fitxer>
```

### Fes commit del fitxers marcats per commit (staged)

```bash
git commit
```
T'obrira vim (o l editor que tinguis definit a $EDITOR) per que escriguis el titol i 
detalls del commit (si el commit tanca un error escriu close #n, on n es el numero del error a github)
 (per escriure a vim s'ha de premer `i`. per guardar `<ESC>:wq<RETURN>`
 
### pull
Actualitzar canvis de github (branca master) a copia local. Sempre fer pull de master abans de fer un push.
```bash
git pull
```
### push

Pujar els canvis de la teva branca local a github. No es pot fer push directament a master, per tant sempre s'ha d haver 
creat i fet checkout d'una branca abans de fer el push. (**IMPORTANT:*** Els commits han d'haver-se fet a la branca, no a master.)
```bash
# Si no havies fet push de la branca a github abans:
git push -u origin <nom-branca>

# Si ja havies fet push de la branca a github:
git push
```
Abans de fer un push a github, es recomenable fer checkout de master, pull (de master), tornar a la branca i fer: `git rebase master` per
facilitar el pull request:
```bash
git checkout master
git pull
git checkout <nom-branca>
git rebase master
git push -u origin
```

### log
Mostra l'historial de commits.
```bash
git log
```

### Stash
Si no deixa fer pull per que teniu canvis respecte al origin que no voleu grardar podeu fer `git stash push`. Aquesta commanda mou els canvis no guardats a
l'*stash* es poden recuperar els canvis amb `git stash pull` o buidar l'*stash* amb `git stash clear` (No es pot desfer el clear, anar amb compte).

### He fet commits a master en comptes de la branca correcte
**DANGER:** es poden perdre commits si es fa malament. Només aplicar el mètode si saps que estas fent.
```bash
# Si no tenies  branca creada:
git checkout -b <nom-branca>
git checkout master
git reset --hard HEAD~<num-de-commits> # HEAD~4 si tens 4 comits a treure de master


# Si ja tenies branca:
git checkout <nom-branca>
git merge master
git reset --hard HEAD~<num-de-commits> # HEAD~4 si tens 4 comits a treure de master
```

