# light.csv

A minimal CSV library for Clojure.

## Usage

##### Parse a string

```clojure
(light.csv/parse-string "First Name, Last Name\nDaniel,Keane\nEric,Blair" :headers? true :keyed? true)]
```

Results in:

```clojure
({:first-name "Daniel" :last-name "Keane"}
 {:first-name "Eric" :last-name "Blair"})
```

## Running Tests

`make tests`

## Build JAR

`make make target/light.csv.jar`