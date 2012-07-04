(ns practical-clojure.core
  (:require [clojure.xml :as xml]))

(defn -main
  "I don't do a whole lot."
  [& args]
  (println "Hello, World!"))


;;; Chapter 3 - Control flow


;; Define a simple function
(fn [x y] (* x y))
(def my-mult (fn [x y] (* x y)))

;; we can also call it in line
((fn [x y] (* x y)) 3 4)

;; defn is shorthand and allows a doc string
(defn squares
  "Return the square of a number"
  [x]
  (* x x))

;; multiple arities in a function
(defn surprise
  ([] 0)
  ([x] (* x x))
  ([x y] (* x y)))

;; multiple arguments in a function
(defn multi-arg
  "Returns the first argument + the number of additional arguments."
  [first & more]
  (+ first (count more)))

;; shorthand for fn, this is useful for inlining
(def sq #(* % %))
(#(* %1 %2) 3 4)

;; conditional expression
(defn piet
  [temp]
  (cond (< temp 20) "It is cold"
        (> temp 25) "It is hot"
        :else "It is just fine"))


;; local bindings with "let"
(let [a 2 b 3 c 4] (+ a b c))

;; condider
(defn seconds-to-weeks
  "Converts seconds to weeks"
  [seconds]
  (/ (/ (/ (/ seconds 60) 60) 24) 7))

(defn seconds-to-weeks
  [seconds]
  (let [minutes (/ seconds 60)
        hours (/ minutes 60)
        days (/ hours 24)
        weeks (/ days 7)]
    weeks))

;;;  Recursion

;; Add-up no tail recursion
(defn add-up
  "Add up numbers below a given limit"
  ([limit] (add-up limit 0 0))
  ([limit current sum]
     (if (= current limit)
       sum
       (add-up limit (+ 1 current) (+ current sum)))))

(defn add-up2
  "Add up numbers below a given limit w/ recur"
  ([limit] (add-up2 limit 0 0))
  ([limit current sum]
     (if (= current limit)
       sum
       (recur limit (+ 1 current) (+ current sum)))))

;;; Higher order function. A higher order function is a function
;;; that take other function(s) as arguments.
(defn arg-switch
  "A higher order function that applies a fun to arguments
   in both orders"
  [fun a b]
  (list (fun a b) (fun b a)))

;;; We can also create functions that return functions
(defn arg-checker
  "Creates function that determines if a number is in a range"
  [min max]
  (fn [x] (and (<= min x) (<= x max))))

;;; We can use 'partial' to curry functions
(def times-pi (partial * 3.14159))
(times-pi 2)

;;; We use 'comp' to compose functions
(def my-fun (comp - *))
(my-fun 5 3)