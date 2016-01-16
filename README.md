# verbal-exprejon

## Usage

FIXME
=======
# Verbal-Exprejon

## Usage

``` clojure

(def url
  (->> (vex)
       (then "http")
       (maybe "s")
       (then "://")
       (maybe "www.")
       (anything-but " ")
       (end-of-line)))

(re-find url "http://www.google.com")

```
## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
