(ns practical-clojure.ch5)

;;; Ch 5 - Sequences ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(first '(1 2 3 4))
(first [1 2 3 4])
(first #{1 2 3 4})
(first {1 :a 2 :b})

(rest '(1 2 3 4))
(rest [1 2 3 4])
(rest #{1 2 3 4})
(rest {:a 1 :b 2 :c 3})

;; We can iterate over sequences
(defn print-all
  [s]
  (if (not (empty? s))
    (do
      (println "Item: " (first s))
      (recur (rest s)))))
(print-all ["java" "clojure" "lisp"])
(print-all #{1 2 3})
(print-all "Hello world")

(cons 4 #{1 2 3})
(conj #{1 2 3} 4)

;; Recursively create list up to a value
(defn make-range [max]
  (loop [acc nil n max]
    (if (zero? n)
      acc
      (recur (cons n acc) (dec n)))))
(make-range 10)

;; Lazy sequences

;; map actually returns a lazy sequence
(defn noisy-square [x]
  (do (println "processing " x)
      (* x x)))

(map noisy-square '(1 2 3))

(def result (map noisy-square '(1 2 3)))

(nth result 2)
(println result)

;; creating lazy sequences directlty
(defn lazy-counter [base increment]
  (lazy-seq
   (cons base (lazy-counter (+ base increment) increment))))

(nth (lazy-counter 0 2) 500000)

;; using iterate to construct lazy sequences. There are other
;; functions that return a lazy sequence.
(def integers (iterate inc 0))
(take 10 integers)
(nth integers 100000)

;; With lazy sequences you have to take care not to keep references to
;; the materialized values unless you want to hog your memory :-)
(nth (iterate inc 0) 100000)

;;; The Sequence API

;; seq - creates sequences
(seq [1 2 3 4 5])
(seq {:a 1 :b 2 :c 3})
(seq #{:a :b :c})

;; vals - gives the values from a map
(vals {:a 1 :b 2 :c 3})

;; keys - gives the keys from a map
(keys {:a 1 :b 2 :c 3})

;; rseq - gives a reverse sequence, arg must be a vector or sortedmap
(rseq [1 2 3 4])
(rseq (sorted-map :a 1 :b 2 :c 3))

;; lazy-seq - gives a lazy sequence (we already saw this)

;; repeatedly - calls a no-arg function repeatedly
(take 5 (repeatedly (fn [] "hello")))
(take 5 (repeatedly (fn [] (rand-int 5))))

;; iterate - calls a 1-arg function, feeding output to next call
(take 5 (iterate (fn [x] (* x x)) 3))
(take 5 (iterate (fn [x] (inc x)) 6))