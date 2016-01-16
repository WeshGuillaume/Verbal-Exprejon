<br>
<h1 align="center">Verbal Exprejon</h1>

<p align="center">
  <a href="https://travis-ci.org/GuillaumeBadi/Verbal-Exprejon"><img src="https://travis-ci.org/GuillaumeBadi/Verbal-Exprejon.svg?branch=master" alt="travis"></a>
</p>

<p align="center">
<br>
|
<b><a href="#introduction"> Introduction </a></b>|
<b><a href="#usage"> Usage </a></b>|

</p>
<br>

---
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
