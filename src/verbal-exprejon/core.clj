(ns regex.core
  (:use [clojure.string :as str]))

(defn compile
  "compile the final regex")

(defn re-add
  "Add a new part to the regex"
  [regex add]
  (re-pattern (str regex add)))

(defn anything
  [regex]
  (re-add regex "(.*)"))

(defn anything-but
  [value regex]
  (re-add regex (str "([^" value "]*)")))

(defn maybe
  [value regex]
  (re-add regex (str "(" value ")?")))

(defn end-of-line
  [regex]
  (re-add regex "$"))

(defn start-of-line
  [regex]
  (re-add regex "^"))

(defn then
  [value regex]
  (re-add regex (str "(" value ")")))

(defn any
  [value regex]
  (re-add regex (str "([" value "])")))

(defn line-break
  [regex]
  (re-add regex "(\\n|(\\r\\n))"))

(defn range
  [args regex]
  (re-add regex (str "([" (str/join "" (map (fn [r] (join "-" r)) (partition 2 args))) "])")))

(defn tab
  [regex]
  (re-add regex "\\t"))

(defn word
  [regex]
  (re-add regex "\\w"))

(defn OR
  [value regex]
  (do (re-add regex "|") ((then value) regex)))

(defn case-insensitive
  [regex]
  (re-add regex "(?i)"))

(def url
  (->> (vex)
       (then "http")
       (maybe "s")
       (then "://")
       (maybe "www.")
       (anything-but " ")
       (end-of-line)))

(defn vex [] #"")
