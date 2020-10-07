# light.csv

A minimal CSV library for Clojure.

## Usage

##### Parse a string

```clojure
(light.csv/parse-string "First Name, Last Name\nDaniel,Keane\nEric,Blair" 
                        :headers? true 
                        :keyed? true)
```

#### Parse a file

```clojure
(light.csv/parse-file "/path/to/file.csv" 
                      :headers? true 
                      :keyed? true 
                      :remove-bom? true)
```

Results in:

```clojure
({:first-name "Daniel" :last-name "Keane"}
 {:first-name "Eric" :last-name "Blair"})
```

## Running Tests

`make tests`
