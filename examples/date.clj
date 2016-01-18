(ns verbal-exprejon.examples.date
  (:require [verbal-exprejon.core :refer :all]))


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
