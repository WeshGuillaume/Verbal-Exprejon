<br>
<h1 align="center">Verbal Exprejon</h1>

<p align="center"><a href="https://github.com/VerbalExpressions/JSVerbalExpressions/">VerbalExpressions</a> with a lisp</p>
<p align="center">
  <a href="https://travis-ci.org/GuillaumeBadi/Verbal-Exprejon"><img src="https://travis-ci.org/GuillaumeBadi/Verbal-Exprejon.svg?branch=master" alt="travis"></a>
  <a href="https://github.com/GuillaumeBadi/vimconfig"><img src="http://imgh.us/Slice_1_2.svg" width="98" alt="travis"></a>
</p>
<br>

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

Match stuff like `https://www.facebook.com facebook` or `https://www.google.com google`

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

### Examples:

``` clojure

(defpattern hour-pattern
  "Matches an hour"
  [] (->> (interval [\0 \9])
          (times [1 2])
          (any-blank)
          (maybe (OR ["h" (->> (then "hour") (maybe "s"))]))))

(defpattern minute-pattern
  "Matches a minute"
  [] (->> (interval [\0 \9])
          (times [1 2])
          (any-blank)
          (maybe (OR ["m" (->> (then "minute") (maybe "s"))]))))

(defpattern second-pattern
  "Matches a second"
  [] (->> (interval [\0 \9])
          (times [1 2])
          (any-blank)
          (maybe (OR ["s" (->> (then "second") (maybe "s"))]))))

(defpattern time-separator
  "Matches the separation between hours, minutes and seconds"
  [] (maybe (->> (any-blank)
                 (maybe (OR ["and" " " ", " ":"]))
                 (any-blank))))

(defpattern time-pattern
  "Matches a time"
  [] (->> (maybe (->> (hour)
                      (time-separator)))
          (maybe (->> (minute)
                      (time-separator)))
          (maybe (second))))

(def time? (matcher time-pattern))

(defn testit
  [time]
  (time? time))

(testit "12h")
(testit "12h5")
(testit "12h05")
(testit "12h30m")
(testit "12:07")
(testit "12hours")
(testit "12hours and 3 minutes")
(testit "12:5:4")
(testit "12h and 5 minutes")
(testit "5 hours")
(testit "12 minutes")
(testit "12 minutes and 5 seconds")
(testit "5s")

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

This has been done by a Clojure newbie.

Copyright © 2016 Guillaume Badi

Distributed under the MIT License
