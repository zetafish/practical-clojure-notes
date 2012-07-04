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

;; repeat - can take 1 or 2 arguments
(take 5 (repeat "hello"))
(take 5 (repeat 1000 :b))

;; range - can take end, begin+end, begin+end+increment
(take 5 (range 100))
(take 5 (range 100 103))
(take 5 (range 100 200 17))

;; distinct - removes duplicates
(take 5 (distinct [1 1 2 2 3 3 3 4 5 6]))

;; filter - remove items not satisfying a predicate
(take 5 (filter (fn [x] (= 0 (mod x 3))) (range 100)))
(filter (fn [x] (= \s (first x))) ["apple" "snake"])

;; remove - remove items satisfying a predicate
(take 5 (remove (fn [x] (= 0 (mod x 3))) (range 100)))

;; cons - add a value to a sequence (always returns a list)
(cons 1 [2 3 4 5])
(cons 1 '(2 3 4 5))
(cons 1 #{ 2 3 4 5})
(cons 1 #{:a 1 :b 2})

;; lazy-cat - lazy version of concat
(lazy-cat [1 2 3] '(4 5 6) #{7 8 9})

;; mapcat - maps a fn to list of sequences and then cat them the idea
;; is that the fn takes a sequence and returns a sequence.
(mapcat (fn [x] x) [ [1 2] [3 4] ])
(mapcat reverse [[1 2 3] [40 50 60] [700 800 900]])

;; cycle - cycles over a sequence
(take 11 (cycle [:a :b :c]))

;; interleave - interleaves a number of sequences, stop at shortest
(interleave [1 2 3] [:a :b :c :d] ["dog" "cat"])

;; interpose - puts a value between items of a sequence
(interpose "barf" [1 2 3 4])

;; rest - sequence except the first, empty sequence gives ()
(rest (range 5))
(rest [])

;; next - sequence except the first - empty sequence gives nil
(next (range 5))
(next [])

;; drop - remove first n items, () if nothing to drop
(drop 5 (range 10))
(drop 5 [])

;; drop-while - remove items while a condition holds
(drop-while #(= % 3) [ 3 3 3 1 2 3 4])

;; take - give first n items, () if nothing present
(take 5 (range 10))
(take 5 (range 3))
(take 5 ())

;; take-nth - give 1st and every nth item
(take-nth 5 (range 30))
(take-nth 5 ())

;; take-while - items while a condition holds
(take-while pos? [1 2 3 -1 4 5 6])

;; drop-last - remove last n items
(drop-last (range 10))
(drop-last 5 (range 10))

;; reverse - reverse sequence
(reverse [1 2 3 4])
(reverse #{1 2 3 4})
(reverse {:a 1 :b 2})

;; sort - sorted sequence
(sort [ 4 3 1 2])
(sort {:b 2 :c 3 :a 1})

;; sort-by - sorted sequence with key fn
(sort-by (fn [x] (mod x 5)) (range 10))

;; split-at - split sequence at [0..n) [n..)
(split-at 5 (range 20))

;; split-with - split sequence allong predicate
(split-with pos? [1 2 3 -5 -6 8])

;; partition - split sequence into parts, only full parts
(partition 3 [:a :b :c :d :e :f :g])

;; partition - split sequence into parts, with child offset
(partition 3 1 [:a :b :c :d :e :f :g])
(partition 3 2 [:a :b :c :d :e :f :g])
(partition 3 4 [:a :b :c :d :e :f :g])

;; map - yes we know this
(map pos? [1 2 3 -4 -5 3])
(map + [1 2 3] [4 5 6])
(map + [1 2 3] [10 20 30] [100 200 300])

;; first - first item, nothing then nil
(first [1 2 3])
(first [])

;; second - second item or nil if not found
(second [1 2 3])
(second [1])
(second [])

;; nth - nth item (zero based!), out-of-bound if not found
(nth [0 1 2 3 4 5 6] 5)
(nth [0 1 2 3 4 5 6] 10)

;; last - last item or nil
(last [1 2 3])
(last [])

;; reduce - reduce itens in sequence to 1 value
(reduce + (range 10))
(reduce (fn [acc val]
          (assoc acc val (/ val)))
        {}
        (range 1 10))

;; apply - apply fn to args and items of a seq (similar to reduce)
(apply + 1 [1 2 3])
(apply + (range 10))

;; empty? - is the sequence empty
(empty? [1 2])
(empty? [])

;; some - is there an item satisfying predicate
(some pos? [1 2 3 -4 -5])
(some pos? [-1 -2])

;; every? - does predcate hold for all
(every? pos? [1 2 3])
(every? pos? [1 2 3 -1])

;; dorun - materialize a sequence for side effects
(dorun (map println (range 10)))
(dorun 4 (map println (range 10)))

;; doall - return materialized sequence
(doall (map println (range 40 46)))