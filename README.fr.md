# Interpréteur de systèmes de Lindenmayer

Langue: [EN](README.en.md) | [FR](README.fr.md)

C'est un interpréteur de L-systèmes écrit en Java. La visualisation en 2D et 3D est implémentée.

Les systèmes déterministes, stochastiques et certains systèmes contextuels (avec des limitations techniques) sont implémentés.

C'était un projet d'équipe sur lequel j'ai travaillé pendant un semestre.

## Utilisation
Pour compiler et exécuter le projet avec **ANT**, utilisez la commande suivante :
> ant run -Dfile={filepath} -Dturtle={2d|3d} -Dtext={y|n}

Pour une exécution directe avec **Java**, il est impératif d'utiliser les options suivantes pour autoriser l'accès aux modules graphiques :
> java --add-exports=java.desktop/sun.awt=ALL-UNNAMED --add-opens=java.desktop/sun.awt=ALL-UNNAMED -jar Project-Lindemeyer.jar {filepath} {2d|3d} {y|n}

Si 3e argument est 'y', le resultat text de fractal sera sauvé dans un fichier txt

## Configuration d'un système
Pour générer une fractale, vous devez créer un fichier de configuration contenant les propriétés suivantes :
- **Axiom** : L'état initial du système.
- **Iteration** : Le nombre de cycles de développement.
- **Rule** : Les règles de production (transformation des symboles).
- **Action** : Les commandes associées aux mouvements de la tortue.

### Exemple de fichier de configuration :
```text
axiom = A
rule = A, ^\AB^\ABA+B^//ABA_B-//ABA+B/A+/

iteration = 3

action = B, move 50
action = +, left 90
action = -, right 90
action = ^, up 90
action = _, down 90
action = /, rollR 90
action = \, rollL 90
action = [, push
action = ], pop
```

## Règles stochastiques (Probabilités)
Il est possible de définir des règles stochastiques avec des probabilités différentes de la manière suivante :
```
rule = F, F[+F][-F]F:0.33
rule = F, F[+F]F:0.33
rule = F, F[-F]F:0.34
```
Syntaxe: `rule = {prédécesseur}, {production}:{probabilité}`

Note : La somme des probabilités pour un même prédécesseur doit être égale à 1.

## Règles contextuelles
Il est possible de définir des règles contextuelles avec différents contextes.
```
rule = A<F>B, F+F
rule = X<F, F-F
rule = F>X, FF
```
Syntaxe : `règle = {contexte-gauche}<{prédécesseur}>{contexte-droit}, {production}`