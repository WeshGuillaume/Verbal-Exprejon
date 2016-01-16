(ns verbal-exprejon.core
  (:use [clojure.string :only (join)])
  (:use [clojure.pprint]))

(defn matcher
  "Transform a pattern into a test matching function"
  [pattern & args]
  (fn
    [candidate]
    (not (nil? (re-matches (->> (apply pattern args)) candidate)))))

(defn re-add
  "Add a new part to the regex"
  [regex add]
  (re-pattern (str regex add)))

(defmacro defpattern
  ([fn-name param-vector body]
   (defpattern fn-name "" param-vector body))
  ([fn-name documentation param-vector body]
   `(def ~fn-name
       (fn
         ([~@param-vector]
          (~fn-name ~@param-vector (vex)))
         ([~@param-vector regex#]
          (re-add regex# ~body))))))

(defn vex [] #"")

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

(defpattern any
  "Matches any of the letters in value"
  [value] (str "([" value "])"))

(defpattern line-break
  "Matches a line break"
  [] "(\\n|(\\r\\n))")

(defpattern interval
  "Matches a range of values.
   Example (range \\a \\z \\A \\Z 0 9)
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
   Example: (OR)
   FIX"
  [value] (then value #"|"))

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
        (anything-but " ")))

(defpattern url-name
  "Match if it is a domain url followed by its name"
  [domain]
  (->>  (vex)
        (url? domain)
        (then " ")
        (then domain)))

(defn hour?
  ([] (hour? (vex)))
  ([regex]
   (re-add regex (->> (interval [\0 \9])
                      (times [1 2])
                      (maybe " ")
                      (maybe (->> (then "h")
                                  (OR (->> (maybe " ")
                                           (then "hour")
                                           (maybe "s")))))))))

(defn minute?
 ([] (minute? (vex)))
 ([regex]
  (re-add regex (->> (interval [\0 \9])
                     (times 1 2)
                     (maybe (->> (maybe " ")
                                 (then "m")
                                 (OR (->> (maybe " ")
                                          (then "minute")
                                          (maybe "s")))))))))


(defn second?
 ([] (second? (vex)))
 ([regex]
  (re-add regex (->> (interval [\0 \9])
                     (times 1 2)
                     (maybe (->> (maybe " ")
                                 (OR (->> (maybe " ")
                                          (then "second")
                                          (maybe "s")))))))))

(defn time?
  ([] (time? (vex)))
  ([regex]
   (re-add regex (->> (hour?)
                      (maybe (->> (maybe (->> (then " ") (OR " and ") (OR ", ") (OR ":")))
                                  (minute?)
                                  (maybe (->> (maybe (->> (then " ") (OR " and ") (OR ", ") (OR ":")))
                                              (second?)))))))))

(defn testit
  [time]
  (not (nil? (re-matches (->> (time?)) time))))

; (testit "12h")
; (testit "12h5")
; (testit "12h05")
; (testit "12h30m")
; (testit "12:07")
; (testit "12hours")
; (testit "12hours and 3 minutes")
; (testit "12:5:4")
; (testit "12h and 5 minutes")
; (testit "5 hours")
; (testit "12 minutes")
