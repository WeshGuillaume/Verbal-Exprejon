(ns verbal-exprejon.core-test
  (:require [clojure.test :refer :all]
            [verbal-exprejon.core :refer :all]))

(defn is-not-nil
  [a] (is (not (nil? a))))

(defn is-nil
  [a] (is (nil? a)))

(deftest anything-test
  (testing "anything"
    (let [testfn #(re-matches (->> (vex) (anything)) %)]
      (is #"(.*)" (->> (vex) (anything)))
      (is-not-nil (testfn "abcdef01234"))
      (is-not-nil (testfn "")))))

(deftest maybe-test
  (testing "maybe"
    (let [testfn #(re-matches (->> (vex) (maybe "Hello")) %)]
      (is #"(Hello)?" (->> (vex) (maybe "Hello")))
      (is-not-nil (testfn "Hello"))
      (is-not-nil (testfn "")))))

(deftest eof-test
  (testing "end-of-line"
    (let [testfn #(re-matches (->> (vex) (end-of-line)) %)]
      (is #"$" (->> (vex) (end-of-line)))
      (is-not-nil (testfn "")))))

(deftest start-test
  (testing "start-of-line"
    (is #"^" (->> (vex) (start-of-line)))))

(deftest then-test
  (testing "then"
    (let [testfn #(re-matches (->> (vex) (then "Hello")) %)]
      (is #"(Hello)" (->> (vex) (then "Hello")))
      (is-not-nil (testfn "Hello"))
      (is-nil (testfn "World")))))

(deftest any-test
  (testing "any"
    (let [testfn #(re-matches (->> (vex) (any "abc")) %)]
      (is #"([abc])" (->> (vex) (any "abc")))
      (is-not-nil (testfn "a"))
      (is-not-nil (testfn "a"))
      (is-not-nil (testfn "c"))
      (is-nil (testfn "e"))
      (is-nil (testfn "abc")))))

(deftest br-test
  (testing "line-break"
    (is #"(\n|(\r\n))" (->> (vex) (line-break)))))

(deftest interval-test
  (testing "interval"
    (let [testfn #(re-matches (->> (vex) (interval [\a \z \A \Z])) %)]
      (is #"[a-zA-Z]" (->> (vex) (interval [\a \z \A \Z])))
      (is-not-nil (testfn "z"))
      (is-not-nil (testfn "B"))
      (is-nil (testfn "0"))
      (is-nil (testfn "9")))))

(deftest teb-test
  (testing "tab"
    (is #"  " (->> (vex) (tab)))))


(deftest word-test
  (testing "word"
    (let [testfn #(re-matches (->> (vex) (word)) %)]
      (is #"([a-zA-Z]+)" (->> (vex) (word)))
      (is-not-nil (testfn "a"))
      (is-not-nil (testfn "Hello"))
      (is-nil (testfn "213")))))
