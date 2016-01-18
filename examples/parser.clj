
(ns vebral-exprejon.parser
  (:require [verbal-exprejon.core :refer :all]))

(comment
  "
  # Cest beau

  add = (a b) => { a + b }
  subs = (a b) => { a - b }
  subs 3 4, add 1 == 0

  factorial = (n <= 1) => 1
  factorial = (n) => factorial n - 1

  nil? = #(% != nil)

  get '/factorial' #( %1 :value, factorial, render %2 )

  get '/factorial'
      ({ value <= 1000 } { render }) => { factorial value, render }

  google = request 'http://google.com' | puts | get 'id' | puts

  when google { puts 'done' }

  defn test = ( value ) => { println value + 1 }
")

(defpattern block
  "Between { and }
   TODO:
    - every expression is a block"
  [] (->> (any-blank)
          (then "\\{")
          (any-blank)
          (maybe (anything))
          (any-blank)
          (then "\\}")
          (any-blank)))

(print-pattern (block))

(defpattern statements
  "A statement is an instruction
   Either an assignment, a function call etc"
  [] (->> (assignment)))

(defpattern parameters-list
  "A list of parameters used to declare functions
   They are separated by (any-blank)
   TODO:
    - add typed parameters
    - define 'a list of parameters inside parens'"
  [] (->> (then "\\(")
          (words-split-by ["\\t" "\\s" "\\n" ","])
          (then "\\)")))

(defpattern function-definition
  "Matches a function of type (a b) => { a + b }
  parameters-list blank => blank block "
  [] (->> (parameters-list)
          (any-blank)
          (then "=>")
          (any-blank)
          (block)))

(defpattern expression
  "Almost anything"
  [] (->> (function-definition)))

(defn identifier
  "Simple word with allowed chars"
  [] (word))

(defpattern assignment
  "a = b"
  [] (->> (identifier)
          (any-blank)
          (then "=")
          (any-blank)
          (expression)))

(let [block?          (matcher block)
      param-list?     (matcher parameters-list)
      function-def?   (matcher function-definition)
      expression?     (matcher expression)
      assignment?     (matcher assignment)
      identifier?     (matcher identifier)]
  (println (block? "



  ")))
