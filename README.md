<br>
<h1 align="center">Verbal Exprejon</h1>

<p align="center">VerbalExpressions with a lisp</p>
<p align="center">
  <a href="https://travis-ci.org/GuillaumeBadi/Verbal-Exprejon"><img src="https://travis-ci.org/GuillaumeBadi/Verbal-Exprejon.svg?branch=master" alt="travis"></a>
</p>
<br>
<p align="center">
|
<b><a href="#usage"> Usage </a></b>|

</p>
<br>

---
## Usage

### Create a URL matcher

``` clojure

(def url
  (->> (vex)
       (then "http")
       (maybe "s")
       (then "://")
       (maybe "www.")
       (anything-but " ")
       (end-of-line)))

(re-matches url "http://www.google.com")

```

### Create your own midleware

A middleware is a function that takes a regex as its last parameter,
and output a regex.

``` clojure

; Match a `domain` url like https://domain.com

(defn url?
  [domain regex]
  (re-add regex (->>  (vex)
                      (then "http")
                      (maybe "s")
                      (then "://")
                      (maybe "www.")
                      (then domain)
                      (anything-but " "))))

```

### Reuse your middlewares

Match stuff like `https://www.google.com Hello` or `https://www.google.com World`

``` clojure

(def url-name
  (->>  (vex)
        (url? "google")
        (then " ")
        (anything-but " ")))

(re-matches url-name "https://www.google.com Google")

```

## License

Copyright © 2016 Guillaume Badi

Distributed under the MIT License
