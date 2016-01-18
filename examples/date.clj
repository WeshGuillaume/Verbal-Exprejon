(ns verbal-exprejon.examples.date
  (:require [verbal-exprejon.core :refer :all]))

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

(defpattern absolute-time-pattern
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
