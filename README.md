# Advent of Code 2023 in Kotlin

## Setup

- create `local/cookie` file with content like
```properties
session=5361......bb4c6
```
(grab the cookie from DevTools after logging to AoC)

- each day at 6AM (CET) run
```bash
DAY=`date "+%-d"` ; curl -v -b `cat local/cookie` https://adventofcode.com/2023/day/${DAY}/input -o local/day${DAY}_input.txt
```
