cs-draw
[![Travis](https://img.shields.io/travis/sergey-astapov/cs-draw.svg)](https://travis-ci.org/sergey-astapov/cs-draw)
=========

**Draw** is a drawing console application

Main flow:

1. Create a new canvas
1. Start drawing on the canvas by issuing various commands
3. Quit

## Commands

```
C w h           Creates a new canvas of width w and height h.
L x1 y1 x2 y2   Creates a new line from (x1,y1) to (x2,y2). Currently only
                horizontal or vertical lines are supported. Horizontal and vertical lines
                will be drawn using the 'x' character.
R x1 y1 x2 y2   Creates a new rectangle, whose upper left corner is (x1,y1) and
                lower right corner is (x2,y2). Horizontal and vertical lines will be drawn
                using the 'x' character.
B x y c         Fills the entire area connected to (x,y) with "colour" c. The
                behaviour of this is the same as that of the "bucket fill" tool in paint
                programs.
Q               Quits the program.
```

## Examples

### Canvas Example
```
enter command: C 20 4
----------------------
|                    |
|                    |
|                    |
|                    |
----------------------
```

### Line Example
```
enter command: L 1 2 6 2
----------------------
|                    |
|xxxxxx              |
|                    |
|                    |
----------------------

enter command: L 6 3 6 4
----------------------
|                    |
|xxxxxx              |
|     x              |
|     x              |
----------------------
```

### Rectangle Example
```
enter command: R 14 1 18 3
----------------------
|             xxxxx  |
|xxxxxx       x   x  |
|     x       xxxxx  |
|     x              |
----------------------
```

### Bucket Fill Example
```
enter command: B 10 3 o
----------------------
|oooooooooooooxxxxxoo|
|xxxxxxooooooox   xoo|
|     xoooooooxxxxxoo|
|     xoooooooooooooo|
----------------------
```

### Quit Example
```
enter command: Q
```

## How to build

```
./gradlew assemble
```

## How to run

```
cd build/libs
java -jar draw-<VERSION>.jar
```

## How to run from IDE

```
Main class: io.draw.ConsoleDrawApp
```

## Assumptions

### Commands Format

* Commands should not start with spaces
* One space is delimiter

### Width and Height Restrictions

Bucket fill functionality bases on recursive algorithm which has next width-height restrictions:

```
    Area = width * height <= 6400
```