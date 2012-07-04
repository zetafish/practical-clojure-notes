(ns practical-clojure.ch4
  (:require clojure.set))

;;; Chapter 4 - Data
(+ 1 2)
(+ 1 2 3)

(- 5)
(- 5 1)
(- 5 1 2)

(string? 1)
(string? "Hello")

(number? 1)
(number? "Hello")

(zero? 0)
(zero? 1E-1000)
(zero? 1E-100)

;;; Regular expressions
(re-pattern "[a-zA-Z]*")
#"[a-zA-Z]"

;; re-matches finds the regular expression match or nil
(re-matches #"[a-zA-Z]*" "test")
(re-matches #"[a-zA-Z]*" "123")

;;; Collections

;; Lists
(list 1 2 3 4)
(peek '(1 2 3 4))
(pop '(1 2 3 4))
(list? '(1 2 3 4))

;; Vectors
(vector 1 2 3 4)
[1 2 3 4]
(vec '( 1 2 3 4))
(get ["first" "second" "third"] 0)
(peek [1 2 3 4])
(vector? [1 2 3 4])
(conj [1 2] :a :b :c)
(assoc [1 2 3 4] 0 "asdf")
(assoc [5 6 7 8] 3 "whaaa")
(pop [1 2 3 4])
(subvec [1 2 3 4 5 6] 2)
(subvec [1 2 3 4 5 6] 2 5)


;; Maps
(def my-map {:a 1 :b 2 :c 3})
(def my-map {:a 1, :b 2, :c 3})
(hash-map :a 1 :b 2 :c 3 :d 4)
(sorted-map :a 1 :b 2 :c 3 :d 4)

;; Struct maps
(defstruct person :first-name :last-name)
(def alan (struct-map person :first-name "alan" :last-name "kay"))
;; create high performance accessor
(def get-first-name (accessor person :first-name))

;; Maps as objects.
;;   Clojure uses maps as objects. Maps provide a generic
;;   interface.

;; assoc - makes a new map with new assoc-iations
(assoc {:a 1 :b 2 :c 3} :kas 90)
(assoc {:a 1 :b 2 :c 3} :kas 90 :barf 88)

;; dissoc - makes a new map with items dissoc-iated
(dissoc {:a 1 :b 2  :c 3} :a)
(dissoc {:a 1 :b 2  :c 3} :a :b)

;; conj - extends map with new key/value pairs
(conj {:a 1 :b 2 :c 3} {:d 4})
(conj {:a 1 :b 2 :c 3} {:d 4 :e 5})

;; merge - merge maps, last value wins
(merge {:a 1 :b 2} {:c 3 :d 4})
(merge {:a 1 :b 2} {:b 10 :c 3 :d 4})
(merge {:a 1 :b 2} {:b 10 :c 3 :d 4} {:b 900})

;; merge-with - merges map, combining values with a func
(merge-with + {:a 1 :b 2} {:b 3 :c 4})
(merge-with + {:a 1 :b 2} {:b 3 :c 4} {:b 100 :c -10})

;; get - find an  association
(get {:a 1 :b 2 :c 3} :a)
(get {:a 1 :b 2 :c 3} :d)
(get {:a 1 :b 2 :c 3} :d "fantasy")

;; contains? - is an item in the map
(contains? {:a 1 :b 2 :c 3} :c)
(contains? {:a 1 :b 2 :c 3} :d)

;; map? - is something a map
(map? 1)
(map? {:a 1})

;; keys - the keys of the map
(keys {})
(keys {:a 1 :b 2})

;; vals - the values of the map
(vals {})
(vals {:a 1 :b 2})

;;; Sets ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def languages #{"perl" "java" "clojure" "c++"})
(hash-set '(1 2 3 4 5 6))
(sorted-set [1 2 3])

;; set keys are the vals
(languages "perl")
(languages "scala")

;; Common set functions
(clojure.set/union #{1 2} #{3 4})
(clojure.set/intersection #{1 2 3 4} #{3 4 5 6})
(clojure.set/difference #{1 2 3 4} #{4 5 6})