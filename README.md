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
  (->> (then "http")
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

(defpattern url?
  "Match if it is a url"
  [domain]
  (->>  (then "http")
        (maybe "s")
        (then "://")
        (maybe "www.")
        (then domain)
        (anything-but " ")))

```

### Reuse your middlewares

Match stuff like `https://www.google.com Hello` or `https://www.google.com World`

``` clojure

(defpattern url-name
  "Match if it is a domain url followed by its name"
  [domain]
  (->>  (url? domain)
        (then " ")
        (then domain)))

(def match (matcher url-name "google"))
(println (match "https://www.google.com google"))

;; -> true

```

## License

Copyright © 2016 Guillaume Badi

Distributed under the MIT License
