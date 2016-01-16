(ns verbal-exprejon.core
  (:use [clojure.string :only (join)]))

(defn- re-add
  "Add a new part to the regex"
  [regex add]
  (re-pattern (str regex add)))

(defn anything
  "Matches everything"
  [regex]
  (re-add regex "(.*)"))

(defn anything-but
  "Matching everything but the specified value"
  [value regex]
  (re-add regex (str "([^" value "]*)")))

(defn maybe
  "Eventually match the provided value"
  [value regex]
  (re-add regex (str "(" value ")?")))

(defn end-of-line
  "Matches the end of the line"
  [regex]
  (re-add regex "$"))

(defn start-of-line
  "Matches the start of the line"
  [regex]
  (re-add regex "^"))

(defn then
  "Matches the specified value, literally"
  [value regex]
  (re-add regex (str "(" value ")")))

(defn any
  "Matches any of the letters in value"
  [value regex]
  (re-add regex (str "([" value "])")))

(defn line-break
  "Matches a line break"
  [regex]
  (re-add regex "(\\n|(\\r\\n))"))

(defn interval
  "Matches a range of values.
   Example (range \\a \\z \\A \\Z 0 9)
   will match every characters between
   a and z,
   A and Z,
   0 and 9"
  [args regex]
  (re-add regex
          (str "(["
               (join "" (map (fn [r] (join "-" r)) (partition 2 args)))
               "])")))

(defn tab
  "Matches a tabulation"
  [regex]
  (re-add regex " "))

(defn word
  "Matches any word (case-insensitive)"
  [regex]
  (re-add regex "[a-zA-Z]+"))

(defn OR
  "OR operator.
   Example: (OR)
   FIX"
  [value regex]
  (do (re-add regex "|") ((then value) regex)))

(defn case-insensitive
  "FIX"
  [regex]
  (re-add regex "(?i)"))

(defn vex [] #"")

(defn url?
  [domain regex]
  (re-add regex (->>  (vex)
                      (then "http")
                      (maybe "s")
                      (then "://")
                      (maybe "www.")
                      (then domain)
                      (end-of-line))))

(def url-name
  (->>  (vex)
        (url? "google")
        (then " ")
        (anything-but " ")))

(re-matches url-name "https://www.google.com Google")
