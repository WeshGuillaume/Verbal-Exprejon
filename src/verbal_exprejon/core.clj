(ns verbal-exprejon.core
  (:use [clojure.string :only (join)])
  (:use [clojure.pprint]))

(defn vex [] #"")

(defn matcher
  "Transform a pattern into a test matching function"
  [pattern & args]
  (fn
    [candidate]
    (not (nil? (re-matches (apply pattern args) candidate)))))

(defn print-pattern
  "Print the resulting pattern"
  [pattern] (println pattern))

(defn re-add
  "Add a new part to the regex"
  [regex add]
  (re-pattern (str regex add)))

(defmacro defpattern
  [fn-name documentation param-vector body]
  `(def ~fn-name
      (fn
        ([~@param-vector]
         (~fn-name ~@param-vector (vex)))
        ([~@param-vector regex#]
         (re-add regex# ~body)))))

(defpattern one-or-more
  "One or more of the previous middleware"
  [] "+")

(defpattern zero-or-more
  "Zero or more of the previous middleware"
  [] "*")

(defpattern any
  "Matches any of the letters in value"
  [value] (str "([" value "])"))

(defpattern any-blank
  "sequence of blanks characters"
  [] (->> (any "\\s\\t\\n")
          (zero-or-more)))

(defpattern anything
  "Matches everything"
  [] "(.*)")

(defpattern anything-but
  "Matching everything but the specified value"
  [value] (str "([^" value "]*)"))

(defpattern maybe
  "Eventually match the provided value"
  [value] (str "(" value ")?"))

(defpattern end-of-line
  "Matches the end of the line"
  [] "$")

(defpattern start-of-line
  "Matches the start of the line"
  [] "^")

(defpattern then
  "Matches the specified value, literally"
  [value] (str "(" value ")"))

(defpattern line-break
  "Matches a line break"
  [] "(\\n|(\\r\\n))")

(defpattern interval
  "Matches a range of values.
   Example (interval \\a \\z \\A \\Z 0 9)
   will match every characters between
   a and z,
   A and Z,
   0 and 9"
  [args] (str "(["
               (join "" (map (fn [r] (join "-" r)) (partition 2 args)))
               "])"))

(defpattern tab
  "Matches a tabulation"
  [] " ")

(defpattern word
  "Matches any word (case-insensitive)"
  [] "[a-zA-Z]+")

(defn OR
  "OR operator.
   Example: (OR ['value 1' 'value 2'])"
  [values] (str "(" (join "|" values) ")"))

(defpattern times
  "Specify the interval"
  [[start end]] (str "{" start "," end "}"))

(defpattern url?
  "Match if it is a url"
  [domain]
  (->>  (then "http")
        (maybe "s")
        (then "://")
        (maybe "www.")
        (then domain)
        (then ".")
        (anything-but " ")))

(defpattern words-split-by
  "Matches a sequence of words delimited by any of the characters
   in ignore-vector"
  [ignore-vector]
  (let [any-delimiter (join "|" ignore-vector)]
    (str "(((" any-delimiter ")+)?([a-zA-Z]+))+")))

(defpattern sentence
  "Matches a sentence of words split by spaces"
  [] (->> (words-split-by ["\\s"])))
