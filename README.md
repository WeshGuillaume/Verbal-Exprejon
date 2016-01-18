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

### Create a Regex

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

### Reference

`(then "string")`:

Matches the string literally

`(maybe "string")`:

Matches the string if any

`(anything)`:

Matches anything

`(anything-but "string")`:

Matches anything except the provided value

`(one-or-more)`:

Matches the previous middleware one ore more times

`(zero-or-more)`:

Matches the previous middleware zero or more times

`(any "letters")`:

Matches any letter from the provided string

`(any-blank)`:

Matches any blank characters including line breaks, spaces and tabs

`(end-of-line)`:

Matches the end of a line

`(start-of-line)`:

Matches the start of a line

`(line-break)`:

Matches a \n

`(interval [characters])`:

Matches the pairs ranges provided.
Examples:
``` clojure

;; matches any letter between a-z and A-Z
(interval \a \z \A \Z)

```

`(tab)`:

Matches a tabulation

`(word)`:

Matches a word (case insensitive)

`(OR [vector])`:

Matches the first matching expression in the vector

`(times [start end])`:

Matches the previous middleware from `start` times to `end` times
Example:

``` clojure
; [0-9]{1,3}

(interval \0 \9)
(times [1 3])

```

`(words-split-by [delimiters])`:

Matches a sequence of words split by any delimiters in the provided vector

`(sentence)`:

Matches a sequence of words split by spaces

## License

Copyright © 2016 Guillaume Badi

Distributed under the MIT License
